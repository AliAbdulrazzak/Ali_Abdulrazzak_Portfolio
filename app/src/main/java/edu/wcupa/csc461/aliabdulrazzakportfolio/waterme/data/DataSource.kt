package edu.wcupa.csc461.aliabdulrazzakportfolio.waterme.data

import edu.wcupa.csc461.aliabdulrazzakportfolio.R
import edu.wcupa.csc461.aliabdulrazzakportfolio.waterme.model.Plant

object DataSource {
    val plants = listOf(
        Plant(
            id = "lithop",
            name = R.string.waterme_lithop,
            schedule = R.string.waterme_monthly,
            type = R.string.waterme_succulent,
            description = R.string.waterme_stone_mimicking_succulent,
            emoji = "🪨",
            recommendedIntervalDays = 30,
            sunlight = R.string.waterme_sun_full,
            soil = R.string.waterme_soil_sandy,
            funFact = R.string.waterme_fact_lithop
        ),
        Plant(
            id = "carrot",
            name = R.string.waterme_carrot,
            schedule = R.string.waterme_daily,
            type = R.string.waterme_root,
            description = R.string.waterme_hardy_root_vegetable,
            emoji = "🥕",
            recommendedIntervalDays = 1,
            sunlight = R.string.waterme_sun_full,
            soil = R.string.waterme_soil_loamy,
            funFact = R.string.waterme_fact_carrot
        ),
        Plant(
            id = "peony",
            name = R.string.waterme_peony,
            schedule = R.string.waterme_weekly,
            type = R.string.waterme_flower,
            description = R.string.waterme_spring_blooming_flower,
            emoji = "🌸",
            recommendedIntervalDays = 7,
            sunlight = R.string.waterme_sun_partial,
            soil = R.string.waterme_soil_rich,
            funFact = R.string.waterme_fact_peony
        ),
        Plant(
            id = "pothos",
            name = R.string.waterme_pothos,
            schedule = R.string.waterme_weekly,
            type = R.string.waterme_houseplant,
            description = R.string.waterme_indoor_vine,
            emoji = "🌱",
            recommendedIntervalDays = 7,
            sunlight = R.string.waterme_sun_indirect,
            soil = R.string.waterme_soil_potting,
            funFact = R.string.waterme_fact_pothos
        ),
        Plant(
            id = "fiddle_leaf_fig",
            name = R.string.waterme_fiddle_leaf_fig,
            schedule = R.string.waterme_weekly,
            type = R.string.waterme_broadleaf_evergreen,
            description = R.string.waterme_ornamental_fig,
            emoji = "🍂",
            recommendedIntervalDays = 7,
            sunlight = R.string.waterme_sun_bright_indirect,
            soil = R.string.waterme_soil_well_drained,
            funFact = R.string.waterme_fact_fiddle
        ),
        Plant(
            id = "strawberry",
            name = R.string.waterme_strawberry,
            schedule = R.string.waterme_daily,
            type = R.string.waterme_fruit,
            description = R.string.waterme_delicious_multiple_fruit,
            emoji = "🍓",
            recommendedIntervalDays = 1,
            sunlight = R.string.waterme_sun_full,
            soil = R.string.waterme_soil_loamy,
            funFact = R.string.waterme_fact_strawberry
        )
    )
}
