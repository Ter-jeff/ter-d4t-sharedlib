// Mutation testing is expensive (hours, not minutes), so it's gated to the branches where it's
// worth the cost: main itself, or a branch already attached to an open pull request. env.CHANGE_ID
// is only set when Jenkins is building a PR-specific ref (not just any branch that happens to have
// one open) -- most branch sources have no trait distinguishing draft PRs from ready ones, so "has
// an open PR" is the closest available signal for "ready" rather than a true draft check.
def call() {
    if (env.BRANCH_NAME == 'main') {
        return true
    }
    return env.CHANGE_ID != null
}
