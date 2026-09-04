// Sends the standard post-build notification email. `config` is the parsed
// .devops/config.json for the calling pipeline; recipients come from
// config.Settings.EMAILS if set, otherwise falls back to whatever
// recipientProviders/default recipients are configured on the Jenkins side.
def call(config) {
    def emailBody = '$DEFAULT_CONTENT <br /> <br /> <div style="padding-left: 30px; padding-bottom: 15px;"> ${CHANGES, showPaths=true, format="<div><b>%a</b>: %r %p </div><div style=\\"padding-left:30px;\\"> &#8212; &#8220;<em>%m</em>&#8221;</div>", pathFormat="</div><div style=\\"padding-left:30px;\\">%p"} </div>'

    def emailArgs = [
        subject: '$DEFAULT_SUBJECT',
        body: emailBody,
        mimeType: 'text/html',
        attachLog: true,
        from: 'tagswbuild@teradyne.com',
    ]

    def emails = config?.Settings?.EMAILS
    if (emails) {
        emailArgs.to = emails
    }

    emailext(emailArgs)
}
