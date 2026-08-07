package week11.st482988.recipeai_finalproject.data

import week11.st482988.recipeai_finalproject.data.model.Recipe

val sampleRecipes = listOf(
    Recipe(
        title = "Margherita Pizza",
        description = "Classic Neapolitan pizza with tomato, mozzarella, and fresh basil.",
        imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Margherita_Originale.JPG/960px-Margherita_Originale.JPG",
        ingredients = listOf("Pizza dough", "San Marzano tomatoes", "Fresh mozzarella", "Basil leaves", "Olive oil", "Salt"),
        instructions = listOf(
            "Preheat oven to 500°F (260°C) with a pizza stone if you have one.",
            "Stretch the dough into a 12-inch round on a floured surface.",
            "Spread crushed tomatoes evenly, leaving a border for the crust.",
            "Tear mozzarella into pieces and scatter over the top.",
            "Bake 8-10 minutes until the crust is golden and cheese is bubbling.",
            "Top with fresh basil and a drizzle of olive oil before serving."
        ),
        cookingTime = 25,
        difficulty = "Easy",
        cuisine = "Italian",
        rating = 4.6,
        reviewCount = 0,
        mealType = "Dinner",
        dietaryRestrictions = listOf("Vegetarian"),
        servings = 4
    ),
    Recipe(
        title = "Beef Tacos",
        description = "Street-style tacos with seasoned beef, onion, and cilantro.",
        imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/7/73/001_Tacos_de_carnitas%2C_carne_asada_y_al_pastor.jpg/960px-001_Tacos_de_carnitas%2C_carne_asada_y_al_pastor.jpg",
        ingredients = listOf("Ground beef", "Corn tortillas", "White onion", "Cilantro", "Lime", "Cumin", "Chili powder"),
        instructions = listOf(
            "Brown the ground beef in a skillet over medium-high heat.",
            "Season with cumin, chili powder, and salt; cook until fully browned.",
            "Warm the tortillas on a dry skillet or open flame.",
            "Fill each tortilla with beef, diced onion, and chopped cilantro.",
            "Finish with a squeeze of fresh lime juice."
        ),
        cookingTime = 20,
        difficulty = "Easy",
        cuisine = "Mexican",
        rating = 4.5,
        reviewCount = 0,
        mealType = "Dinner",
        dietaryRestrictions = emptyList(),
        servings = 4
    ),
    Recipe(
        title = "Vegetable Stir Fry",
        description = "Quick, colorful stir fry with a savory garlic-ginger sauce.",
        imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/2/25/Chinese_vegetable_stir_fry_noodles.jpg/960px-Chinese_vegetable_stir_fry_noodles.jpg",
        ingredients = listOf("Broccoli", "Bell pepper", "Carrot", "Garlic", "Ginger", "Soy sauce", "Sesame oil", "Noodles"),
        instructions = listOf(
            "Heat sesame oil in a wok over high heat.",
            "Add garlic and ginger, stir for 30 seconds until fragrant.",
            "Add vegetables and stir fry 4-5 minutes until crisp-tender.",
            "Toss in cooked noodles and soy sauce.",
            "Stir everything together for 1-2 more minutes and serve hot."
        ),
        cookingTime = 20,
        difficulty = "Easy",
        cuisine = "Asian",
        rating = 4.4,
        reviewCount = 0,
        mealType = "Dinner",
        dietaryRestrictions = listOf("Vegetarian", "Vegan"),
        servings = 2
    ),
    Recipe(
        title = "Pumpkin Soup",
        description = "Creamy roasted pumpkin soup finished with a touch of cream.",
        imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/b/bc/Pumpkin_soup_with_groundnuts_and_nutritious_spices_served_in_a_clay_pot.jpg/960px-Pumpkin_soup_with_groundnuts_and_nutritious_spices_served_in_a_clay_pot.jpg",
        ingredients = listOf("Pumpkin", "Onion", "Garlic", "Vegetable broth", "Cream", "Salt", "Pepper"),
        instructions = listOf(
            "Cut the pumpkin into chunks, removing skin and seeds.",
            "Sauté onion and garlic until soft, then add pumpkin.",
            "Pour in broth to just cover the pumpkin and simmer until tender.",
            "Blend until smooth using a stick blender.",
            "Stir in cream, season to taste, and serve warm."
        ),
        cookingTime = 30,
        difficulty = "Easy",
        cuisine = "American",
        rating = 4.7,
        reviewCount = 0,
        mealType = "Lunch",
        dietaryRestrictions = listOf("Vegetarian", "Gluten-free"),
        servings = 4
    ),
    Recipe(
        title = "Spaghetti Carbonara",
        description = "Roman pasta classic with egg, pecorino, and crispy pancetta.",
        imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/3/33/Espaguetis_carbonara.jpg/960px-Espaguetis_carbonara.jpg",
        ingredients = listOf("Spaghetti", "Pancetta", "Eggs", "Pecorino Romano", "Black pepper"),
        instructions = listOf(
            "Cook spaghetti in salted boiling water until al dente.",
            "Crisp the pancetta in a skillet over medium heat.",
            "Whisk eggs with grated pecorino and lots of black pepper.",
            "Off heat, toss hot pasta with pancetta, then quickly mix in the egg mixture.",
            "Add a splash of pasta water to loosen into a creamy sauce; serve immediately."
        ),
        cookingTime = 25,
        difficulty = "Intermediate",
        cuisine = "Italian",
        rating = 4.8,
        reviewCount = 0,
        mealType = "Dinner",
        dietaryRestrictions = emptyList(),
        servings = 4
    ),
    Recipe(
        title = "Greek Salad",
        description = "Crisp cucumber, tomato, olives, and feta with oregano dressing.",
        imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/1/1b/Greek_salad_and_Tzatziki.jpg/960px-Greek_salad_and_Tzatziki.jpg",
        ingredients = listOf("Cucumber", "Tomato", "Red onion", "Kalamata olives", "Feta cheese", "Olive oil", "Oregano"),
        instructions = listOf(
            "Chop cucumber, tomato, and red onion into bite-sized pieces.",
            "Combine in a bowl with olives and a block of feta on top.",
            "Drizzle with olive oil and sprinkle with dried oregano.",
            "Toss gently just before serving."
        ),
        cookingTime = 15,
        difficulty = "Easy",
        cuisine = "Mediterranean",
        rating = 4.5,
        reviewCount = 0,
        mealType = "Lunch",
        dietaryRestrictions = listOf("Vegetarian", "Gluten-free"),
        servings = 2
    ),
    Recipe(
        title = "Pad Thai",
        description = "Stir-fried rice noodles with shrimp, egg, and tamarind sauce.",
        imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/3/39/Phat_Thai_kung_Chang_Khien_street_stall.jpg/960px-Phat_Thai_kung_Chang_Khien_street_stall.jpg",
        ingredients = listOf("Rice noodles", "Shrimp", "Egg", "Bean sprouts", "Tamarind paste", "Fish sauce", "Peanuts", "Lime"),
        instructions = listOf(
            "Soak rice noodles in warm water until pliable.",
            "Stir fry shrimp until just cooked, push to the side of the pan.",
            "Scramble egg in the same pan, then mix everything together.",
            "Add noodles, tamarind paste, and fish sauce; toss to coat.",
            "Fold in bean sprouts, top with crushed peanuts and a lime wedge."
        ),
        cookingTime = 30,
        difficulty = "Intermediate",
        cuisine = "Asian",
        rating = 4.6,
        reviewCount = 0,
        mealType = "Dinner",
        dietaryRestrictions = emptyList(),
        servings = 2
    ),
    Recipe(
        title = "Chicken Tikka Masala",
        description = "Marinated chicken in a rich, spiced tomato-cream sauce.",
        imageUrl = "https://upload.wikimedia.org/wikipedia/commons/4/44/Chicken_Tikka_Masala_KellySue.JPG",
        ingredients = listOf("Chicken thighs", "Yogurt", "Garam masala", "Tomato sauce", "Cream", "Garlic", "Ginger", "Rice"),
        instructions = listOf(
            "Marinate chicken in yogurt and spices for at least 30 minutes.",
            "Sear the chicken until browned, then set aside.",
            "Sauté garlic and ginger, then add tomato sauce and simmer.",
            "Return chicken to the pan and stir in cream.",
            "Simmer until chicken is cooked through and sauce thickens; serve over rice."
        ),
        cookingTime = 45,
        difficulty = "Intermediate",
        cuisine = "Asian",
        rating = 4.7,
        reviewCount = 0,
        mealType = "Dinner",
        dietaryRestrictions = listOf("Gluten-free"),
        servings = 4
    )
)