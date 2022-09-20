package dev.punitd.unplashapp.model

val photosSuccessfulResponse = """
    [
      {
        "id": "122",
        "urls": {
          "regular": "https://images.unsplash.com/photo-1657664042448-c955b411d9d0?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwzNDQyNTJ8MXwxfGFsbHwxfHx8fHx8Mnx8MTY2MjQ1OTAxNA&ixlib=rb-1.2.1&q=80&w=1080"
        },
        "likes": 20,
        "user": {
          "username": "oppofindx5pro",
          "links": {
            "html": "https://unsplash.com/@oppofindx5pro"
          },
          "profile_image": {
            "medium": "https://images.unsplash.com/profile-1657663575361-5e8d57088720image?ixlib=rb-1.2.1&crop=faces&fit=crop&w=64&h=64"
          }
        }
      },
      {
        "id": "123",
        "urls": {
          "regular": "https://images.unsplash.com/photo-1661956602926-db6b25f75947?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwzNDQyNTJ8MXwxfGFsbHwxfHx8fHx8Mnx8MTY2Mjk2ODEwMA&ixlib=rb-1.2.1&q=80&w=1080"
        },
        "likes": 230,
        "user": {
          "username": "mailchimp",
          "links": {
            "html": "https://unsplash.com/@mailchimp"
          },
          "profile_image": {
            "medium": "https://images.unsplash.com/profile-1609545740442-928866556c38image?ixlib=rb-1.2.1&crop=faces&fit=crop&w=64&h=64"
          }
        }
      }
    ]
""".trimIndent()

val searchSuccessfulResponse = """
    {
      "results": [
        {
          "id": "122",
          "urls": {
            "regular": "https://images.unsplash.com/photo-1657664042448-c955b411d9d0?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwzNDQyNTJ8MXwxfGFsbHwxfHx8fHx8Mnx8MTY2MjQ1OTAxNA&ixlib=rb-1.2.1&q=80&w=1080"
          },
          "likes": 20,
          "user": {
            "username": "oppofindx5pro",
            "links": {
              "html": "https://unsplash.com/@oppofindx5pro"
            },
            "profile_image": {
              "medium": "https://images.unsplash.com/profile-1657663575361-5e8d57088720image?ixlib=rb-1.2.1&crop=faces&fit=crop&w=64&h=64"
            }
          }
        },
        {
          "id": "123",
          "urls": {
            "regular": "https://images.unsplash.com/photo-1661956602926-db6b25f75947?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwzNDQyNTJ8MXwxfGFsbHwxfHx8fHx8Mnx8MTY2Mjk2ODEwMA&ixlib=rb-1.2.1&q=80&w=1080"
          },
          "likes": 230,
          "user": {
            "username": "mailchimp",
            "links": {
              "html": "https://unsplash.com/@mailchimp"
            },
            "profile_image": {
              "medium": "https://images.unsplash.com/profile-1609545740442-928866556c38image?ixlib=rb-1.2.1&crop=faces&fit=crop&w=64&h=64"
            }
          }
        }
      ]
    }
""".trimIndent()

val authErrorMessage = "OAuth error: The access token is invalid"
val errorMessage = "Invalid request"
val authErrorResponse = """
    {
    	"errors": [
    		"$authErrorMessage"
    	]
    }
""".trimIndent()
val errorResponse = """
    {
    	"errors": [
    		"$errorMessage"
    	]
    }
""".trimIndent()
