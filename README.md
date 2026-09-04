# ter-d4t-sharedlib

Jenkins Pipeline shared library for D4T projects. Loaded via:

```groovy
@Library(['ter-d4t-sharedlib']) _
```

## Steps

Each file under [`vars/`](vars) is one global pipeline step, callable directly
by filename once the library is loaded (no `pipelineUtils.` prefix needed):

- **`sendBuildEmail(config)`** — sends the standard post-build notification
  email. `config` is the parsed `.devops/config.json` of the calling
  pipeline; recipients come from `config.Settings.EMAILS`.
- **`publishCoverageStatus()`** — posts a coverage status to the GitHub
  Statuses API via `.devops/coverage_post_github_status.py`, using the
  `GitHub-Account` credential. Never fails the build (catches `Throwable`).
  Works on both Windows and Unix agents.
- **`shouldRunOnMainOrReadyMr()`** — `true` on `main` or on a branch attached
  to an open pull request; used to gate expensive stages (e.g. mutation
  testing) to where the cost is worth it.

## One-time Jenkins setup

**Manage Jenkins → System → Global Pipeline Libraries → Add**

- Name: `ter-d4t-sharedlib` (must match the `@Library(...)` name exactly)
- Default version: `main`
- Retrieval method: Modern SCM → Git → `https://github.com/Ter-jeff/ter-d4t-sharedlib.git`

## History

Previously each consuming repo (e.g. `test`) carried its own copy of these
functions in a local `.devops/PipelineUtils.groovy`, loaded via `load(...)`
instead of `@Library`, as a stand-in for when this library didn't exist yet.
