// Neither githubNotify nor publishChecks is usable on our Jenkins instance (no GitHub plugin,
// no GitHub App configured for Checks). Reuse the GitHub-Account credential the checkout stages
// already authenticate with instead -- a plain PAT (GIT_ASKPASS-based, not SSH) that also works
// against the classic Statuses API. Never fails the build over a PR-visible nicety: catches
// Throwable, not Exception, since a missing/mistyped credential can surface as an Error (e.g.
// NoSuchMethodError did when githubNotify turned out not to exist here).
//
// Cross-platform: picks sh/bat and the matching path separator based on the agent OS, since this
// step runs on both Windows and macOS/Linux agents depending on the calling pipeline.
def call() {
    try {
        withCredentials([usernamePassword(credentialsId: 'GitHub-Account', usernameVariable: 'GH_USER', passwordVariable: 'GITHUB_STATUS_TOKEN')]) {
            if (isUnix()) {
                sh 'python3 .devops/coverage_post_github_status.py TestResults'
            } else {
                bat '@py .devops\\coverage_post_github_status.py TestResults'
            }
        }
    } catch (Throwable t) {
        echo "WARNING: could not publish coverage status to GitHub: ${t}"
    }
}
