import hudson.model.Result
import jenkins.model.CauseOfInterruption

import jenkins.model.*;

// setup job names here
[
'sample-project'
].each { jobName ->
  def queue = Jenkins.instance.queue
  def q = queue.items.findAll { it.task.name.equals(jobName) }
  def r = [:]
  def projs = jenkins.model.Jenkins.instance.items.findAll { it.name.equals(jobName) }

  projs.each{p ->
    x = p._getRuns()
    x.each{id, y -> 
    r.put(id, y)
    }
  }

  TreeMap queuedMap = [:]
  TreeMap executingMap = [:]
  q.each{i->
    queuedMap.put(i.getId(), i.getCauses()[0].getShortDescription()) //first line
  }
  r.each{id, run->
    def exec = run.getExecutor()
    if(exec != null){
      executingMap.put(id, run.getCauses()[0].getShortDescription()) //first line
    }
  }

  println("Queued:")
  queuedMap.each{ k, v -> println "${k}:${v}" }
  println("Executing:")
  executingMap.each{ k, v -> println "${k}:${v}" }

  // First, if there is more than one queued entry, cancel all but the highest one.
  // Afterwards, if there is a queued entry, cancel the running ones

  def queuedNames = queuedMap.values();
  queuedNames.each{n -> 
    def idsForName = []
    queuedMap.each{ id, name -> 
      if(name.equals(n)){
        idsForName.add(id)
      }
    }
    if (idsForName.size() > 1){
      println("Cancelling queued job: "+n)
          }
    // remove all but the latest from queue
    idsForName.sort().take(idsForName.size() - 1).each { queue.doCancelItem(it) }
  }
  executingMap.each{ id, name -> 
    if(queuedMap.values().contains(name)){
      r.each{rid, run->
        if (id == rid){
          def exec = run.getExecutor()
          if(exec != null){
            println("Aborting running job: "+id+": "+name)
            exec.interrupt(Result.ABORTED)
          }
        }
      }
    }
  }
}
