node ('prodserver') {
    
 notify('Started')    
 
 stage ('SCM_Checkout') {
    checkout([$class: 'GitSCM', 
        branches: [[name: '*/master']], 
        doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], 
        userRemoteConfigs: [[url: '<path to git repo>']]])
 }
 stage ('Build_Test and Package') {
    sh 'mvn clean verify package'
 //   junit 'target/surefire-reports/TEST*.xml'
 }
 
 stage ('Archive and Notify') {
    publishHTML(target: [allowMissing: true, 
                 alwaysLinkToLastBuild: false, 
                 keepAll: true, 
                 reportDir: 'target/site/jacoco', 
                 reportFiles: 'index.html', 
                 reportName: 'HTML Report', 
                 reportTitles: 'Code Coverage-Report'])

    archiveArtifacts 'target/*.war' 
    step([$class: 'Mailer', 
        notifyEveryUnstableBuild: true, 
        recipients: 'cc:<valid-email-id>', 
        sendToIndividuals: false])
 }
 notify ('Waiting for Deployment')
 
 input 'Deploy to Staging?'
 
 stage ('Deploy to AppServer') {
 sh 'cp target/petclinic.war /opt/apache-tomcat-8.5.21/webapps'
 sh 'sudo /opt/apache-tomcat-8.5.21/bin/shutdown.sh'
 sh 'sudo /opt/apache-tomcat-8.5.21/bin/startup.sh'
 }
 
 sh 'git push https://<username>:<password>@github.com/<path-to-git-repo> --all'
 
 notify('Completed')  
}

 def notify(status) {
  mail (
        body:"""${status}: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':
                 Check console output at, 
                 href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]""",
        cc: '<valid-email-id>', 
        subject: """JenkinsNotification: ${status}:""", 
        to: '<alid-email-id>'  
       ) 
 }
