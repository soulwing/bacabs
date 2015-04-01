/*
 * File created on Mar 31, 2015
 *
 * Copyright (c) 2015 Carl Harris, Jr
 * and others as noted
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nerdwin15.bacabs.service.git;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.inject.Inject;

import com.nerdwin15.bacabs.ConcreteGitBranch;
import com.nerdwin15.bacabs.GitBranch;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.SshSessionFactory;
import org.eclipse.jgit.transport.TagOpt;
import org.soulwing.cdi.properties.Property;

/**
 * A {@link GitBranchRetrievalService} that retrieves a branch from a local
 * repository clone.
 *
 * @author Carl Harris
 */
@ApplicationScoped
public class LocalGitBranchRetrievalServiceBean
    implements GitBranchRetrievalService {

  public static final String REMOTE_BRANCH_FORMAT = "refs/remotes/%s/%s";

  public static final String GIT_DIR = ".git";
  public static final String REMOTE = "origin";

  @Inject @Property
  protected URI localRepoLocation;

  @Inject @Property
  protected URI remoteRepoLocation;

  @Inject @Property
  protected URL privateKeyLocation;

  private File directory;

  @PostConstruct
  public void init() {
    SshSessionFactory.setInstance(new CustomConfigSessionFactory(privateKeyLocation));
    directory = new File(localRepoLocation);
    if (!directory.exists()) {
      try {
        clone(directory, REMOTE, remoteRepoLocation.toString());
      }
      catch (GitAPIException | IOException ex) {
        throw new RuntimeException(ex);
      }
    }
  }

  @Override
  public void refresh() {
    try {
      fetch(directory, REMOTE);
    }
    catch (GitAPIException | IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public GitBranch retrieveGitBranch(String branch) {
    String refName = String.format(REMOTE_BRANCH_FORMAT, REMOTE, branch);
    try {
      Git git = newGit(directory);
      List<Ref> branches = git.branchList()
          .setListMode(ListBranchCommand.ListMode.REMOTE)
          .call();
      for (Ref ref : branches) {
        if (refName.equals(ref.getName())) {
          return newGitBranch(git, ref);
        }
      }
      return null;
    }
    catch (GitAPIException | IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  private GitBranch newGitBranch(Git git, Ref ref)
      throws GitAPIException, IOException {
    Iterator<RevCommit> commits = git.log()
        .setMaxCount(1)
        .add(ref.getObjectId())
        .call().iterator();
    if (!commits.hasNext()) return null;
    RevCommit commit = commits.next();
    PersonIdent authorIdent = commit.getAuthorIdent();
    ConcreteGitBranch gitBranch = new ConcreteGitBranch();
    gitBranch.setCommitterName(authorIdent.getName());
    gitBranch.setCommitDate(authorIdent.getWhen().toString());
    return gitBranch;
  }

  private Git newGit(File directory) throws IOException {
    Repository repository = new FileRepository(new File(directory, GIT_DIR));
    return new Git(repository);
  }

  private Git clone(File directory, String remote,
                           String url)
      throws GitAPIException, IOException {
    return new CloneCommand()
        .setCloneAllBranches(true)
        .setRemote(remote)
        .setURI(url)
        .setDirectory(directory)
        .setNoCheckout(true)
        .setProgressMonitor(new TextProgressMonitor())
        .call();
  }

  private Git fetch(File directory, String remote)
      throws GitAPIException, IOException {
    Git git = newGit(directory);
    git.fetch()
        .setTagOpt(TagOpt.AUTO_FOLLOW)
        .setProgressMonitor(new TextProgressMonitor())
        .setRemote(remote)
        .call();
    return git;
  }

}
