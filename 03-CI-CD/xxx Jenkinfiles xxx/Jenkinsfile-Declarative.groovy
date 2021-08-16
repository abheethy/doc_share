pipeline { 
    agent any 
    stages {
        stage('SCM_Chekout') { 
            steps { 
                checkout([$class: 'GitSCM', 
                    branches: [[name: '*/master']], 
                    doGenerateSubmoduleConfigurations: false, 
                    extensions: [], 
                    submoduleCfg: [], 
                    userRemoteConfigs: [[url: 'https://github.com/ganeshhp/helloworldweb.git']]])
            }
        }
        stage('Build'){
            steps {
                sh 'mvn -f pom.xml clean package' 
            }
        }
        stage('Deploy') {
            steps {
                sh 'cp target/*.war /opt/tomcat/webapps/'
                sh '/opt/tomcat/bin/catalina.sh run'

            }
        }
    }
}