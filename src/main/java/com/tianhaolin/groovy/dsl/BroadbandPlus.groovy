package com.tianhaolin.groovy.dsl

class BroadbandPlus {
    //后面会说.这个类是我们DSL的核心类
    def rewards = new RewardService()

    def canConsume = { account, media ->
        def now = new Date()
        if (account.mediaList[media]?.after(now))
            return true
        account.points > media.points
    }

    def consume = { account, media ->
        // 第一次消费才奖励
        if (account.mediaList[media.title] == null) {
            def now = new Date()
            account.points -= media.points account.mediaList[media] = now + media.daysAccess // 应用 DSL 奖励规则 rewards.applyRewardsOnConsume(account, media)
        }
    }

    def extend = {account, media, days ->
        if (account.mediaList[media] != null) {
            account.mediaList[media] += days
            println "extend media ${media.title} $days days for ${account.subscriber}"
        }
    }
}