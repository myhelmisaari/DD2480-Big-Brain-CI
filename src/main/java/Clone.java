import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;

public class Clone {
    public static void main(String[] args) throws GitAPIException, IOException {

        File localPath = File.createTempFile("TestGitRepository", "");
        Git.cloneRepository()
                .setURI("https://github.com/github/testrepo.git")
                .setDirectory(localPath) // #1
                .call();
        }
    }