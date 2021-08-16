node {
 
 /** calling notify function*/   
 notify('Started')    

/** mailer built-in jenkins class*/ 
 stage ('Archive and Notify') {
    step([$class: 'Mailer', 
        notifyEveryUnstableBuild: true, 
        recipients: 'cc:ganesh@automationfactory.in', 
        sendToIndividuals: false])
 }

 /** calling notify function*/

 notify ('Waiting for Deployment')
 
 stage ('Deploy to AppServer') {
      
      /** some code*/
 
  }
 
  notify('Completed')  

  /** define function*/

 def notify(status) {
  /** calling built-in jenkind e-mail class*/
  mail (
        body:"""${status}: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':
                 Check console output at, 
                 href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]""",
        cc: 'support@automationfactory.in', 
        subject: """JenkinsNotification: ${status}:""", 
        to: 'ganesh@automationfactory.in'  
       ) 
  }

}
