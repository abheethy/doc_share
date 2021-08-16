node {

stage ('scm checkout') {
checkout([$class: 'GitSCM', 
branches: [[name: '*/master']], 
doGenerateSubmoduleConfigurations: false, 
extensions: [], 
submoduleCfg: [], 
userRemoteConfigs: [[url: 'https://github.com/ganeshhp/helloworldweb.git']]])

}

stage ('build') {
sh 'mvn clean package'
}

stage ('archive artifacts') {
archiveArtifacts 'target/*.war'
}

}