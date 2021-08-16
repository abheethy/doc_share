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

node ('node') {
	notify ( 'deployment job started' )
	
    stage ('scm checkout') {
    checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[url: 'https://github.com/ganeshhp/helloworldweb.git']]])
    }
    
    # build job: 'buildjob', quietPeriod: 3
    
    stage ('build') {
    sh 'mvn -f pom.xml clean package'
    }
    
	notify ( 'Waiting for input' )	
	
	input "deploy to Production now ?"
	
	
    stage ('deploy') {
    sh '''sudo cp /home/vagrant/jenkins/workspace/workspace/build070117/target/*.war /opt/tomcat/apache-tomcat-7.0.78/webapps/
    sudo sh /opt/tomcat/bin/shutdown.sh
    sudo sh /opt/tomcat/bin/startup.sh'''
    
    }
	 notify ( 'Job completed' )
	 
    stage ('archive') {
    archiveArtifacts 'target/*.war'
    }
}