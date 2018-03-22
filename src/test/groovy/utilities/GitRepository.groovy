package utilities

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.storage.file.FileRepositoryBuilder

class GitRepository {

    static initialiseGitRepository(workingDirectory, branch="master", switchTo="master"){
        Repository repo = FileRepositoryBuilder.create(new File(workingDirectory, ".git"))
        repo.create()
        Git git = new Git(repo)
        git.commit().setAuthor("test", "test@test.com").setMessage("init").call()
        if(branch != "master"){
            git.branchCreate().setName(branch as String).call()
        }
        git.checkout().setName(branch as String).call()
        git.add().addFilepattern(".").call()
        git.commit().setAuthor("test", "test@test.com").setMessage("first commit").call()
        git.checkout().setName(switchTo as String).call()
        new Commander().execute("git update-server-info", new File(workingDirectory))
    }
}
