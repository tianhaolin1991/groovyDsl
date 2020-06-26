package com.tianhaolin.groovy.dsl

onConsume = {
    reward ( "观看迪斯尼的电影, 你可以获得 25%的积分." ) {
        allOf {
            condition {
                media.publisher == "Disney"
            }
            condition {
                isVideo
            }
        }
        grant {
            points media.points / 4
        }
    }

    reward ( "查看新发布的媒体，可以延长一天" ) {
        condition {
            isNewRelease
        }
        grant {
            extend 1
        }
    }
}