package com.example.recipeapp.data

object RecipeSamples {
    val sampleRecipes = listOf(
        Recipe(
            id = 1,
            title = "가지두부조림",
            image = "https://yourcdn.com/images/eggplant_tofu.jpg",
            ingredients = listOf("가지", "두부", "간장", "참기름"),
            instructions = """
                1. 가지와 두부를 먹기 좋게 썬다.
                2. 팬에 살짝 굽는다.
                3. 간장 + 참기름 섞은 양념장을 부어 조린다.
            """.trimIndent(),
            likes = 23
        ),
        Recipe(
            id = 2,
            title = "김치볶음밥",
            image = "https://yourcdn.com/images/kimchi_fried_rice.jpg",
            ingredients = listOf("밥", "김치", "계란", "참기름"),
            instructions = """
                1. 김치를 잘게 썬다.
                2. 팬에 김치 → 밥을 넣고 볶는다.
                3. 계란 프라이를 올리고 마무리.
            """.trimIndent(),
            likes = 41
        ),
        Recipe(
            id = 3,
            title = "닭가슴살 샐러드",
            image = "https://yourcdn.com/images/chicken_salad.jpg",
            ingredients = listOf("닭가슴살", "양상추", "방울토마토", "드레싱"),
            instructions = """
                1. 닭가슴살을 삶아 찢는다.
                2. 채소와 함께 접시에 담고 드레싱 뿌린다.
            """.trimIndent(),
            likes = 15
        )
    )
}
