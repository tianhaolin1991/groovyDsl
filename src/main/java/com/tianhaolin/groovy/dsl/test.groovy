package com.tianhaolin.groovy.dsl

account = new Account(subscriber: "Mr.tian",plan:"BASIC", points:120, spend:0.0)
terminator = new Media(title:"Terminator", type:"VIDEO",
        newRelease:true, price:2.99, points:30,
        daysAccess:1, publisher:"Fox")
up = new Media(title:"UP", type:"VIDEO", newRelease:true,
        price:3.99, points:40, daysAccess:1,
        publisher:"Disney")
account.addMedia(terminator,terminator.daysAccess)
account.addMedia(up,up.daysAccess)
def rewardService = new RewardService()
rewardService.apply(account,terminator)
rewardService.apply(account,up)
println account