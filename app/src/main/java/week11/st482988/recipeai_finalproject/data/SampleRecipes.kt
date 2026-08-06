package week11.st482988.recipeai_finalproject.data

import week11.st482988.recipeai_finalproject.data.model.Recipe

object SampleRecipes {

    fun getAllRecipes(): List<Recipe> {
        return listOf(
            Recipe(
                id = "1",
                title = "Vegetarian Buddha Bowl",
                description = "Colorful roasted vegetables with quinoa",
                imageUrl = "https://via.placeholder.com/400x300?text=Buddha+Bowl",
                ingredients = listOf("quinoa", "chickpeas", "broccoli", "sweet potato", "tahini"),
                instructions = listOf(
                    "Cook quinoa according to package directions",
                    "Roast vegetables at 400F for 25 minutes",
                    "Mix tahini dressing",
                    "Assemble bowl and serve"
                ),
                cookingTime = 25,
                difficulty = "Easy",
                cuisine = "Middle Eastern",
                rating = 4.8,
                reviewCount = 145,
                mealType = "Lunch",
                dietaryRestrictions = listOf("Vegetarian", "Vegan", "Gluten-Free"),
                servings = 2
            ),
            Recipe(
                id = "2",
                title = "Quick Stir Fry",
                description = "Fast and healthy stir fry with fresh vegetables",
                imageUrl = "https://via.placeholder.com/400x300?text=Stir+Fry",
                ingredients = listOf("broccoli", "bell pepper", "soy sauce", "garlic", "ginger", "sesame oil"),
                instructions = listOf(
                    "Chop all vegetables",
                    "Heat wok or large pan",
                    "Cook garlic and ginger first",
                    "Add vegetables and stir fry for 10 minutes",
                    "Add soy sauce and serve"
                ),
                cookingTime = 15,
                difficulty = "Easy",
                cuisine = "Asian",
                rating = 4.6,
                reviewCount = 210,
                mealType = "Dinner",
                dietaryRestrictions = listOf("Vegetarian", "Dairy-Free"),
                servings = 2
            ),
            Recipe(
                id = "3",
                title = "Grilled Chicken Breast",
                description = "Herb-grilled chicken breast with lemon",
                imageUrl = "https://via.placeholder.com/400x300?text=Grilled+Chicken",
                ingredients = listOf("chicken breast", "olive oil", "lemon", "garlic", "rosemary", "thyme"),
                instructions = listOf(
                    "Marinate chicken with herbs and olive oil for 30 minutes",
                    "Preheat grill to medium-high",
                    "Grill chicken 6-7 minutes per side",
                    "Rest for 5 minutes before serving",
                    "Squeeze fresh lemon over top"
                ),
                cookingTime = 20,
                difficulty = "Easy",
                cuisine = "Mediterranean",
                rating = 4.7,
                reviewCount = 189,
                mealType = "Dinner",
                dietaryRestrictions = listOf("Gluten-Free", "Dairy-Free"),
                servings = 2
            ),
            Recipe(
                id = "4",
                title = "Creamy Pasta Primavera",
                description = "Creamy pasta with fresh seasonal vegetables",
                imageUrl = "https://via.placeholder.com/400x300?text=Pasta",
                ingredients = listOf("pasta", "zucchini", "peas", "heavy cream", "parmesan", "garlic", "butter"),
                instructions = listOf(
                    "Cook pasta according to package directions",
                    "Sauté garlic in butter",
                    "Add vegetables and cook 5 minutes",
                    "Pour in cream and simmer",
                    "Toss pasta with sauce and parmesan"
                ),
                cookingTime = 20,
                difficulty = "Intermediate",
                cuisine = "Italian",
                rating = 4.5,
                reviewCount = 156,
                mealType = "Dinner",
                dietaryRestrictions = listOf("Vegetarian"),
                servings = 4
            ),
            Recipe(
                id = "5",
                title = "Fresh Greek Salad",
                description = "Fresh and light Greek salad with feta",
                imageUrl = "https://via.placeholder.com/400x300?text=Greek+Salad",
                ingredients = listOf("tomato", "cucumber", "feta cheese", "olives", "red onion", "olive oil", "oregano"),
                instructions = listOf(
                    "Chop tomatoes and cucumbers",
                    "Slice red onion thinly",
                    "Combine in large bowl",
                    "Add olives and feta",
                    "Drizzle with olive oil and sprinkle oregano"
                ),
                cookingTime = 10,
                difficulty = "Easy",
                cuisine = "Mediterranean",
                rating = 4.4,
                reviewCount = 267,
                mealType = "Lunch",
                dietaryRestrictions = listOf("Vegetarian", "Gluten-Free"),
                servings = 2
            ),
            Recipe(
                id = "6",
                title = "Baked Salmon with Dill",
                description = "Oven-baked salmon with fresh dill and lemon",
                imageUrl = "https://via.placeholder.com/400x300?text=Salmon",
                ingredients = listOf("salmon fillet", "dill", "lemon", "butter", "garlic", "salt", "pepper"),
                instructions = listOf(
                    "Preheat oven to 400F",
                    "Place salmon on parchment paper",
                    "Top with butter, dill, and lemon",
                    "Bake for 15-18 minutes",
                    "Serve hot with lemon wedges"
                ),
                cookingTime = 18,
                difficulty = "Intermediate",
                cuisine = "Scandinavian",
                rating = 4.7,
                reviewCount = 224,
                mealType = "Dinner",
                dietaryRestrictions = listOf("Gluten-Free", "Dairy-Free"),
                servings = 2
            ),
            Recipe(
                id = "7",
                title = "Warm Vegetable Soup",
                description = "Warm and comforting vegetable soup perfect for cold days",
                imageUrl = "https://via.placeholder.com/400x300?text=Soup",
                ingredients = listOf("carrot", "celery", "onion", "vegetable broth", "tomato", "spinach"),
                instructions = listOf(
                    "Chop vegetables into small pieces",
                    "Sauté onion, celery, and carrot in pot",
                    "Add vegetable broth",
                    "Simmer for 20 minutes",
                    "Add spinach and tomato, cook 5 more minutes"
                ),
                cookingTime = 30,
                difficulty = "Easy",
                cuisine = "International",
                rating = 4.3,
                reviewCount = 198,
                mealType = "Soup",
                dietaryRestrictions = listOf("Vegetarian", "Vegan", "Gluten-Free"),
                servings = 4
            ),
            Recipe(
                id = "8",
                title = "Keto Cauliflower Rice",
                description = "Low-carb rice alternative perfect for keto diet",
                imageUrl = "https://via.placeholder.com/400x300?text=Cauliflower",
                ingredients = listOf("cauliflower", "coconut oil", "garlic", "turmeric", "salt", "pepper"),
                instructions = listOf(
                    "Pulse cauliflower in food processor until rice-sized",
                    "Heat coconut oil in pan",
                    "Add garlic and turmeric",
                    "Stir-fry cauliflower rice for 8-10 minutes",
                    "Season with salt and pepper"
                ),
                cookingTime = 12,
                difficulty = "Easy",
                cuisine = "Asian",
                rating = 4.2,
                reviewCount = 142,
                mealType = "Side",
                dietaryRestrictions = listOf("Keto", "Vegetarian", "Gluten-Free"),
                servings = 3
            ),
            Recipe(
                id = "9",
                title = "No-Bake Chocolate Protein Balls",
                description = "No-bake protein dessert perfect for post-workout snack",
                imageUrl = "https://via.placeholder.com/400x300?text=Protein+Balls",
                ingredients = listOf("cocoa powder", "protein powder", "peanut butter", "honey", "oats"),
                instructions = listOf(
                    "Mix cocoa and protein powder",
                    "Add peanut butter and honey",
                    "Stir until combined",
                    "Roll into balls",
                    "Refrigerate for 30 minutes"
                ),
                cookingTime = 15,
                difficulty = "Easy",
                cuisine = "International",
                rating = 4.6,
                reviewCount = 178,
                mealType = "Dessert",
                dietaryRestrictions = listOf("Vegetarian"),
                servings = 10
            ),
            Recipe(
                id = "10",
                title = "Creamy Arborio Risotto",
                description = "Authentic Italian risotto with mushrooms and truffle oil",
                imageUrl = "https://via.placeholder.com/400x300?text=Risotto",
                ingredients = listOf("arborio rice", "mushroom", "white wine", "parmesan", "butter", "onion", "vegetable broth"),
                instructions = listOf(
                    "Heat broth and keep warm",
                    "Sauté mushrooms and onion",
                    "Add rice and toast for 2 minutes",
                    "Add white wine",
                    "Gradually add warm broth, stirring constantly for 20 minutes",
                    "Finish with butter and parmesan"
                ),
                cookingTime = 35,
                difficulty = "Advanced",
                cuisine = "Italian",
                rating = 4.9,
                reviewCount = 87,
                mealType = "Dinner",
                dietaryRestrictions = listOf("Vegetarian"),
                servings = 2
            )
        )
    }
}