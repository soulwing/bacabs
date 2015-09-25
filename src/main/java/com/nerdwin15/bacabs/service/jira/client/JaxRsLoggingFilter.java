package com.nerdwin15.bacabs.service.jira.client;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.*;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

/**
 * A filter that can be used to log JAX-RS client requests and responses.
 *
 * @author Carl Harris
 */
public class JaxRsLoggingFilter implements ClientRequestFilter,
    ClientResponseFilter {

  private final static char IN = '<';

  private static final char OUT = '>';

  private final JaxRsLoggerAdapter logger;

  private boolean logHeaders = true;

  private boolean logEntity = true;

  private int width = 72;

  /**
   * Constructs a new instance that logs to {@link System#out standard output}.
   */
  public JaxRsLoggingFilter() {
    this(System.out);
  }

  /**
   * Constructs a new instance that writes to the given output stream using
   * the platform default character encoding.
   * @param outputStream stream that will receiving logging output
   */
  public JaxRsLoggingFilter(OutputStream outputStream) {
    this(new OutputStreamWriter(outputStream));
  }

  /**
   * Constructs a new instance that writes to the given writer.
   * @param writer writer that will receive logging output
   */
  public JaxRsLoggingFilter(Writer writer) {
    this(new WriterLoggerAdapter(writer));
  }

  /**
   * Constructs a new instance that writes to the given logger adapter.
   * @param logger logger adapter that will receive logging output
   */
  public JaxRsLoggingFilter(JaxRsLoggerAdapter logger) {
    this.logger = logger;
  }

  @Override
  public void filter(ClientRequestContext request) throws IOException {
    wrapRequest(request);
  }

  @Override
  public void filter(ClientRequestContext request,
      ClientResponseContext response) throws IOException {

    // Defensively closing the request output stream, since some JAX-RS
    // implementations don't seem to do so (notably RestEasy).
    OutputStream outputStream = request.getEntityStream();
    if (outputStream != null) {
      try {
        outputStream.close();
      }
      catch (Exception ex) {
        assert true;  // ignore the error
      }
    }

    wrapResponse(response);
  }

  private void wrapRequest(ClientRequestContext request) {
    OutputStream outputStream = request.getEntityStream();
    if (outputStream == null) return;
    BodyLogger bodyLogger = newBodyLogger(OUT, request.getMediaType());
    RequestLogger requestLogger = new RequestLogger(
        logger, request.getMethod(), request.getUri(),
        request.getStringHeaders(), getWidth(), bodyLogger);
    OutputStream streamWrapper = new LoggingOutputStreamWrapper(
        outputStream, requestLogger);
    request.setEntityStream(streamWrapper);
  }

  private void wrapResponse(ClientResponseContext response) {
    InputStream inputStream = response.getEntityStream();
    if (inputStream == null) return;
    BodyLogger bodyLogger = newBodyLogger(IN, response.getMediaType());
    ResponseLogger responseLogger = new ResponseLogger(
        logger, response.getStatusInfo(), response.getHeaders(),
        getWidth(), bodyLogger);
    InputStream streamWrapper = new LoggingInputStreamWrapper(
        inputStream, responseLogger);
    response.setEntityStream(streamWrapper);
  }

  private BodyLogger newBodyLogger(char direction, MediaType mediaType) {
    Charset charset = textContentType(mediaType);
    if (charset != null) {
      return new StringBodyLogger(charset, direction, getWidth());
    }
    return new ByteArrayBodyLogger(direction);
  }

  private Charset textContentType(MediaType contentType) {
    if (contentType == null) return null;
    final String type = contentType.getType();
    final String subType = contentType.getSubtype();
    if (contentType.equals(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
        || contentType.equals(MediaType.MULTIPART_FORM_DATA_TYPE)) {
      return Charset.forName("UTF-8");
    }
    if ("text".equals(type)) {
      return charset(contentType);
    }
    if ("application".equals(type)) {
      if ("json".equals(subType)) {
        return Charset.forName("UTF-8");
      }
      if ("xml".equals(subType)
          || subType.startsWith("xml-")
          || subType.endsWith("+xml")) {
        Charset charset = charset(contentType);
        if (charset == null) {
          // FIXME -- this is a bad idea and inconsistent with XML spec
          // should be getting the appropriate encoding from the XML header...
          // oh, well
          charset = Charset.forName("UTF-8");
        }
        return charset;
      }
    }
    return null;
  }

  private Charset charset(MediaType contentType) {
    final String charset = contentType.getParameters()
        .get(MediaType.CHARSET_PARAMETER);
    if (charset == null) return null;
    try {
      return Charset.forName(charset);
    }
    catch (UnsupportedCharsetException ex) {
      return null;
    }
  }

  /**
   * Gets the {@code logHeaders} property.
   * @return property value
   */
  public boolean isLogHeaders() {
    return logHeaders;
  }

  /**
   * Sets the {@code logHeaders} property.
   * @param logHeaders the property value to set
   */
  public void setLogHeaders(boolean logHeaders) {
    this.logHeaders = logHeaders;
  }

  /**
   * Gets the {@code logEntity} property.
   * @return property value
   */
  public boolean isLogEntity() {
    return logEntity;
  }

  /**
   * Sets the {@code logEntity} property.
   * @param logEntity the property value to set
   */
  public void setLogEntity(boolean logEntity) {
    this.logEntity = logEntity;
  }

  /**
   * Gets the {@code width} property.
   * @return property value
   */
  public int getWidth() {
    return width;
  }

  /**
   * Sets the {@code width} property.
   * @param width the property value to set
   */
  public void setWidth(int width) {
    this.width = width;
  }

  private interface BodyLogger {

    void log(byte[] body, JaxRsLoggerAdapter logger);

  }

  private static class ByteArrayBodyLogger implements BodyLogger {

    private final char direction;

    public ByteArrayBodyLogger(char direction) {
      this.direction = direction;
    }

    private String offset(int idx) {
      return String.format("%c %08x:", direction, idx);
    }

    private String octet(byte[] buf, int offset) {
      if (offset < buf.length) {
        return String.format("%02x", buf[offset]);
      }
      else {
        return "  ";
      }
    }

    private String octets(byte[] buf, int offset) {
      StringBuilder sb = new StringBuilder();
      for (int j = 0; j < 8; ++j) {
        sb.append(' ');
        sb.append(octet(buf, offset + 2 * j));
        sb.append(octet(buf, offset + 2 * j + 1));
      }
      return sb.toString();
    }

    private String printables(byte[] buf,
        int offset) {
      StringBuilder sb = new StringBuilder();
      for (int j = 0; j < 16; ++j) {
        int c = 0;
        if (offset + j < buf.length) {
          c = buf[offset + j];
          if (c < 32 || c > 126) {
            c = '.';
          }
        }
        else {
          c = ' ';
        }
        sb.append((char) c);
      }
      return sb.toString();
    }

    @Override
    public void log(byte[] body, JaxRsLoggerAdapter logger) {
      if (body.length == 0) return;
      logger.log(JaxRsLoggerAdapter.TRACE, "%c", direction);
      for (int offset = 0; offset < body.length; offset += 16) {
        final StringBuilder sb = new StringBuilder();
        sb.append(offset(offset));
        sb.append(octets(body, offset));
        sb.append("  ");
        sb.append(printables(body, offset));
        logger.log(JaxRsLoggerAdapter.TRACE, sb.toString());
      }
    }

  }

  private static class StringBodyLogger implements BodyLogger {

    private final Charset charset;
    private final char direction;
    private final int width;

    public StringBodyLogger(Charset charset, char direction, int width) {
      this.charset = charset;
      this.direction = direction;
      this.width = width;
    }

    @Override
    public void log(byte[] body, JaxRsLoggerAdapter logger) {
      final String text = new String(body, charset);
      if (text.isEmpty()) return;
      logger.log(JaxRsLoggerAdapter.TRACE, "%c", direction);
      int i = 0;
      final int length = text.length();
      while (i < length) {
        final int k = text.indexOf('\n', i);
        final int lineLength = k >= 0 ? k - i : Integer.MAX_VALUE;
        final int segmentLength = Math.min(width, length - i);
        final int j = i + Math.min(segmentLength, lineLength);
        logger.log(JaxRsLoggerAdapter.TRACE, "%c %s", direction, text.substring(i, j));
        i = j;
        if (k != -1) {
          i++;
        }
      }
    }
  }

  private static abstract class EntityLogger {

    protected final JaxRsLoggerAdapter logger;
    protected final MultivaluedMap<String, String> headers;
    protected final char direction;
    protected final int width;
    protected final BodyLogger bodyLogger;

    protected EntityLogger(JaxRsLoggerAdapter logger,
        MultivaluedMap<String, String> headers,
        char direction, int width, BodyLogger bodyLogger) {
      this.logger = logger;
      this.headers = headers;
      this.direction = direction;
      this.width = width;
      this.bodyLogger = bodyLogger;
    }

    private void logHeaders() {
      for (String key : headers.keySet()) {
        for (Object value : headers.get(key)) {
          logWrappedHeader(key + ": " + value);
        }
      }
    }

    protected void logWrappedHeader(final String text) {
      int i = 0;
      final int length = text.length();
      int j = Math.min(width, length);
      logger.log(JaxRsLoggerAdapter.DEBUG, "%c %s", direction, text.substring(i, j));
      i = j;
      while (i < length) {
        j = i + Math.min(width - 1, length - i);
        logger.log(JaxRsLoggerAdapter.DEBUG, "%c  %s", direction, text.substring(i, j));
        i = j;
      }
    }

    protected abstract void logFirstLine();

    public final void log(byte[] buf) {
      logFirstLine();
      logHeaders();
      bodyLogger.log(buf, logger);
    }

  }

  private static class RequestLogger extends EntityLogger {

    private final String method;
    private final URI uri;

    protected RequestLogger(JaxRsLoggerAdapter logger, String method,
        URI uri, MultivaluedMap<String, String> headers, int width,
        BodyLogger bodyLogger) {
      super(logger, headers, OUT, width, bodyLogger);
      this.method = method;
      this.uri = uri;
    }

    @Override
    protected void logFirstLine() {
      logger.log(JaxRsLoggerAdapter.INFO, "%c %s %s", OUT, method, uri);
    }

  }

  private static class ResponseLogger extends EntityLogger {

    private final Response.StatusType status;

    protected ResponseLogger(JaxRsLoggerAdapter logger,
        Response.StatusType status, MultivaluedMap<String, String> headers,
        int width, BodyLogger bodyLogger) {
      super(logger, headers, IN, width, bodyLogger);
      this.status = status;
    }

    @Override
    protected void logFirstLine() {
      logger.log(JaxRsLoggerAdapter.INFO, "%c %d %s", IN, status.getStatusCode(),
          status.getReasonPhrase());
    }

  }

  private class LoggingInputStreamWrapper extends InputStream {

    private final ByteArrayOutputStream buf = new ByteArrayOutputStream();

    private final InputStream delegate;
    private final ResponseLogger responseLogger;

    public LoggingInputStreamWrapper(InputStream delegate,
        ResponseLogger responseLogger) {
      this.delegate = delegate;
      this.responseLogger = responseLogger;
    }

    @Override
    public int read() throws IOException {
      int c = delegate.read();
      buf.write(c);
      return c;
    }

    @Override
    public int read(byte[] b) throws IOException {
      return read(b, 0, b.length);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
      int numRead = delegate.read(b, off, len);
      if (numRead != -1) {
        buf.write(b, 0, numRead);
      }
      return numRead;
    }

    @Override
    public long skip(long n) throws IOException {
      return delegate.skip(n);
    }

    @Override
    public int available() throws IOException {
      return delegate.available();
    }

    @Override
    public void close() throws IOException {
      buf.close();
      responseLogger.log(buf.toByteArray());
      delegate.close();
    }

    @Override
    public void mark(int readlimit) {
      delegate.mark(readlimit);
    }

    @Override
    public void reset() throws IOException {
      delegate.reset();
    }

    @Override
    public boolean markSupported() {
      return delegate.markSupported();
    }

  }

  private class LoggingOutputStreamWrapper extends OutputStream {

    private final ByteArrayOutputStream buf = new ByteArrayOutputStream();

    private final OutputStream delegate;
    private final RequestLogger requestLogger;


    public LoggingOutputStreamWrapper(OutputStream delegate,
        RequestLogger requestLogger) {
      this.delegate = delegate;
      this.requestLogger = requestLogger;
    }

    @Override
    public void write(int b) throws IOException {
      buf.write(b);
      delegate.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
      write(b, 0, b.length);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
      buf.write(b, off, len);
      delegate.write(b, off, len);
    }

    @Override
    public void flush() throws IOException {
      delegate.flush();
    }

    @Override
    public void close() throws IOException {
      buf.close();
      requestLogger.log(buf.toByteArray());
      delegate.close();
    }

  }

  private static class WriterLoggerAdapter implements JaxRsLoggerAdapter {

    private final PrintWriter writer;

    public WriterLoggerAdapter() {
      this(System.out);
    }

    public WriterLoggerAdapter(OutputStream outputStream) {
      this(new OutputStreamWriter(outputStream));
    }

    public WriterLoggerAdapter(Writer writer) {
      this.writer = writer instanceof PrintWriter ?
          (PrintWriter) writer : new PrintWriter(writer);
    }

    @Override
    public void log(int level, String format, Object... args) {
      writer.format(format, args);
      writer.println();
      writer.flush();
    }

  }

}