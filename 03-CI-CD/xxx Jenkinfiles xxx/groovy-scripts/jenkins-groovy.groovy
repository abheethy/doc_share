node {
	
	stage ('git chekcout') {
		checkout([$class: 'GitSCM', 
		branches: [[name: '*/master']], 
		doGenerateSubmoduleConfigurations: false, 
		extensions: [], submoduleCfg: [], 
		userRemoteConfigs: [[url: 'https://github.com/ganeshhp/helloworldweb.git']]])
	}

	stage ('app build') {

		sh 'mvn clean package'
	}
	
	stage ('Archive') {
	    archiveArtifacts 'target/Helloworldwebapp.war'
	}

	input 'Deploy to Prod server?'

	stage ('deploy') {

		sh 'cp target/Helloworldwebapp.war /opt/tomcat/webapps/'
	}

	mail bcc: '', 
		body: '', cc: '', 
		from: '', 
		replyTo: '', 
		subject: 'Jenkins notification', 
		to: 'ganeshhp@gmail.com'

}