node {
    
    stage ('scm checkout') {
    checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[url: 'https://github.com/ganeshhp/helloworldweb.git']]])
    }
    
    # build job: 'buildjob', quietPeriod: 3
    
    stage ('build') {
    sh 'mvn -f pom.xml clean package'
    }
    
    stage ('deploy') {
    sh '''sudo cp /home/vagrant/jenkins/workspace/workspace/build070117/target/*.war /opt/tomcat/apache-tomcat-7.0.78/webapps/
    sudo sh /opt/tomcat/bin/shutdown.sh
    sudo sh /opt/tomcat/bin/startup.sh'''
    
    }
    stage ('archive') {
    archiveArtifacts 'target/*.war'
    }
}