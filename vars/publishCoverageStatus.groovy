// Neither githubNotify nor publishChecks is usable on our Jenkins instance (no GitHub plugin,
// no GitHub App configured for Checks). Reuse the github-packages-pat secret text credential
// instead -- a plain PAT that also works against the classic Statuses API. Never fails the
// build over a PR-visible nicety: catches Throwable, not Exception, since a missing/mistyped
// credential can surface as an Error (e.g. NoSuchMethodError did when githubNotify turned out
// not to exist here).
//
// Cross-platform: picks sh/bat and the matching path separator based on the agent OS, since this
// step runs on both Windows and macOS/Linux agents depending on the calling pipeline.
def call() {
    try {
        withCredentials([string(credentialsId: 'github-packages-pat', variable: 'GITHUB_STATUS_TOKEN')]) {
            if (isUnix()) {
                sh 'python3 .devops/coverage_post_github_status.py .devops/TestResults'
            } else {
                bat '@py .devops\\coverage_post_github_status.py .devops\\TestResults'
            }
        }
    } catch (Throwable t) {
        echo "WARNING: could not publish coverage status to GitHub: ${t}"
    }
}
