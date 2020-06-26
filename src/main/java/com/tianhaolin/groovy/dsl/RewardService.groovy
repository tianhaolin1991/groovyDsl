package com.tianhaolin.groovy.dsl

class RewardService {
    //服务本身提供的代码桩和默认实现，以及一些标记 static boolean on_consume_provided = true

    static Binding baseBinding = new Binding();

    static {
        loadDSL(baseBinding)
        loadRewardRules(baseBinding)
    }

    //构造 reward、condition、allOf、anyOf、grant 等核心闭包到 binding 中
    //而这些 binding 构建的变量、上下文信息都可以传入给 DSL，让编写 DSL
    //的人员可以利用!
    static void loadDSL(Binding binding) {

        binding.reward = { spec, closure ->
            closure.delegate = delegate
            binding.result = true
            binding.and = true
            closure()
        }

        binding.condition = { closure ->
            closure.delegate = delegate
            if (binding.and)
                binding.result = (closure() && binding.result)
            else
                binding.result = (closure() || binding.result)
        }

        binding.allOf = { closure ->
            //closure.delegate = delegate
            def storeResult = binding.result
            def storeAnd = binding.and
            binding.result = true // Starting premise is true binding.and = true
            closure()
            if (storeAnd) {
                binding.result = (storeResult && binding.result)
            } else {
                binding.result = (storeResult || binding.result)
            }
            binding.and = storeAnd
        }

        binding.anyOf = { closure ->
            closure.delegate = delegate
            def storeResult = binding.result
            def storeAnd = binding.and
            binding.result = false // Starting premise is false binding.and = false
            closure()
            if (storeAnd) {
                binding.result = (storeResult && binding.result)
            } else {
                binding.result = (storeResult || binding.result)
            }
            binding.and = storeAnd
        }

        binding.grant = { closure ->
            closure.delegate = delegate
            if (binding.result)
                closure()
        }

        binding.extend = { days ->
            def bbPlus = new BroadbandPlus()
            bbPlus.extend(binding.account, binding.media, days)
        }

        binding.points = { points ->
            binding.account.points += points
        }

    }

    //构建一些媒体信息和条件短语

    void prepareMedia(binding, media) {
        binding.media = media
        binding.isNewRelease = media.newRelease
        binding.isVideo = (media.type == "VIDEO")
        binding.isGame = (media.type == "GAME")
        binding.isSong = (media.type == "SONG")
    }

    //初始化加载奖赏脚本，在这个脚本中，可以定义 onConsume 等 DSL
    static void loadRewardRules(Binding binding) {
        Binding selfBinding = new Binding()
        GroovyShell shell = new GroovyShell(selfBinding)
        //市场人员写的 DSL 脚本就放在这个文件下，里面定义 onConsume //这些个 rewards 奖励
        shell.evaluate(new File("./rewards.groovy")) //将外部 DSL 定义的消费、购买奖励赋值
        binding.onConsume = selfBinding.onConsume
    }

    //真正的执行方法
    void apply(account, media) {
        Binding binding = baseBinding;
        binding.account = account
        prepareMedia(binding,media)
        GroovyShell shell = new GroovyShell(binding)
        shell.evaluate("onConsume.delegate=this;onConsume()")
    }

}