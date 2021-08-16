node ('master') {

 dir('C:\\Project\\helloworldweb') {
    
/*    stage ('scm checkout') {
    checkout([$class: 'GitSCM', 
        branches: [[name: 'master']], 
        doGenerateSubmoduleConfigurations: false, 
        extensions: [], 
        submoduleCfg: [], 
        userRemoteConfigs: [[url: 'https://github.com/ganeshhp/helloworldweb.git']]])
    }
 */
    stage ('build') {
        bat 'mvn -f pom.xml clean package'
        }
    
    mail bcc: '', 
        body: 'approval', cc: '', from: '', 
        replyTo: '', 
        subject: 'proceed ?', 
        to: 'ganeshhp@gmail.com'
    
    input 'Deploy to Prod server?'

    stage ('deploy') {
      bat 'xcopy C:\\Project\\helloworldweb\\target\\*.war C:\\apache-tomcat-8.5.24\\webapps /Y'
  
    }
    stage ('archive') {
        archiveArtifacts 'target/*.war'
    }

   
  }

}
 