@Library('pfPipeline@tags/4') _

//##################################
//# Template v1.0.1
//##################################

pipeline{

    agent {
        label 'java'
    }

    options {
        // the gitlab connection to use with admpico token
        gitLabConnection('gitlab-dogen.group.echonet')
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '5', artifactNumToKeepStr: '5'))
        timeout(time: 1, unit: 'HOURS')
    }

    parameters {
        booleanParam(name: "RELEASE", description: "Build a release from current commit.", defaultValue: false)
    }

    triggers {
        // start build on gitlab push (need good configuration in gitlab for the project)
        gitlab(triggerOnPush: true, branchFilterType: 'All')

        // build every day
        cron('H H(6-23) * * *')
    }

    stages {
        stage('Build') {
            when {
				anyOf {
					changeRequest()
					branch 'feature/*'
				}
            }
            steps {
                // maven build without push to nexus
                pfMvnBuild(goals: 'clean install sonar:sonar')

                //security analysis
                pfMvnBuild(goals: '-U com.sonatype.clm:clm-maven-plugin:index')
                pfIQServerAnalysis (
                    iqApplication: "IdentifyMeProxy",
                    iqStage: 'build',
                    iqScanPatterns: [[scanPattern: '**/sonatype-clm/module.xml']]
                )
            }
        }

        stage('Build & Deliver') {
            when {
                allOf {
                    expression { !params.RELEASE }
                    anyOf {
				        branch 'master'
				        branch 'release/*'
				    }
				}
            }
            steps {
                // maven build with push to nexus
                pfMvnBuild()

                //security analysis
                pfMvnBuild(goals: '-U com.sonatype.clm:clm-maven-plugin:index')
                pfIQServerAnalysis (
                    iqApplication: "IdentifyMeProxy",
                    iqStage: 'build',
                    iqScanPatterns: [[scanPattern: '**/sonatype-clm/module.xml']]
                )
            }
        }

        stage('Release Build') {
            when {
                allOf {
					expression { params.RELEASE }
                        anyOf {
                            branch 'master'
                            branch 'release/*'
                        }
                }
            }
            steps {
                // do release
                pfMvnRelease()

                //security analysis
                //index is generated during the release build
                pfIQServerAnalysis (
                    //TODO put your application name here
                    iqApplication: "IdentifyMeProxy",
                    iqStage: 'release',
                    iqScanPatterns: [[scanPattern: '**/sonatype-clm/module.xml']]
                )
            }
        }

		stage('Gates') {
		    when {
                anyOf {
                    changeRequest()
                    branch 'feature/*'
                    branch 'master'
                    branch 'release/*'
                }
            }
			steps {
				pfGate()
			}
		}

    }

    post {
             always {
                    //send notification ( mail, gitlab, ... )
                    pfSendNotification(to:'ahmedamine.boulifa@alphacredit.be,jeanphilippe.rolland@alphacredit.be,gangaanjaneyareddy.dontireddy@alphacredit.be');
             }
        }
}