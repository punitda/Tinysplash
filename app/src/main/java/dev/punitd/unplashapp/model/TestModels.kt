package dev.punitd.unplashapp.model

import okhttp3.Headers

val image1 = UnsplashImage(
    id = "122",
    urls = Urls(regularImage = "https://images.unsplash.com/photo-1657664042448-c955b411d9d0?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwzNDQyNTJ8MXwxfGFsbHwxfHx8fHx8Mnx8MTY2MjQ1OTAxNA&ixlib=rb-1.2.1&q=80&w=1080"),
    likes = 20,
    user = User(
        username = "oppofindx5pro",
        userLinks = UserLinks(html = "https://unsplash.com/@oppofindx5pro"),
        profileImage = ProfileImage(medium = "https://images.unsplash.com/profile-1657663575361-5e8d57088720image?ixlib=rb-1.2.1&crop=faces&fit=crop&w=64&h=64")
    )
)

val image2 = UnsplashImage(
    id = "123",
    urls = Urls(regularImage = "https://images.unsplash.com/photo-1661956602926-db6b25f75947?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwzNDQyNTJ8MXwxfGFsbHwxfHx8fHx8Mnx8MTY2Mjk2ODEwMA&ixlib=rb-1.2.1&q=80&w=1080"),
    likes = 230,
    user = User(
        username = "mailchimp",
        userLinks = UserLinks(html = "https://unsplash.com/@mailchimp"),
        profileImage = ProfileImage(medium = "https://images.unsplash.com/profile-1609545740442-928866556c38image?ixlib=rb-1.2.1&crop=faces&fit=crop&w=64&h=64")
    )
)

val image3 = UnsplashImage(
    id = "124",
    urls = Urls(regularImage = "https://images.unsplash.com/photo-1657664042448-c955b411d9d0?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwzNDQyNTJ8MXwxfGFsbHwxfHx8fHx8Mnx8MTY2MjQ1OTAxNA&ixlib=rb-1.2.1&q=80&w=1080"),
    likes = 20,
    user = User(
        username = "oppofindx5pro",
        userLinks = UserLinks(html = "https://unsplash.com/@oppofindx5pro"),
        profileImage = ProfileImage(medium = "https://images.unsplash.com/profile-1657663575361-5e8d57088720image?ixlib=rb-1.2.1&crop=faces&fit=crop&w=64&h=64")
    )
)

val image4 = UnsplashImage(
    id = "125",
    urls = Urls(regularImage = "https://images.unsplash.com/photo-1661956602926-db6b25f75947?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwzNDQyNTJ8MXwxfGFsbHwxfHx8fHx8Mnx8MTY2Mjk2ODEwMA&ixlib=rb-1.2.1&q=80&w=1080"),
    likes = 230,
    user = User(
        username = "mailchimp",
        userLinks = UserLinks(html = "https://unsplash.com/@mailchimp"),
        profileImage = ProfileImage(medium = "https://images.unsplash.com/profile-1609545740442-928866556c38image?ixlib=rb-1.2.1&crop=faces&fit=crop&w=64&h=64")
    )
)


val images = listOf(image1, image2)
val otherImages = listOf(image3, image4)


val pageLinks = PageLinks(
    headers = Headers.Builder().add(
        "link",
        "<https://api.unsplash.com/photos?page=13760&per_page=20>; rel=\"last\", <https://api.unsplash.com/photos?page=2&per_page=20>; rel=\"next\""
    ).build()
)


val otherPageLinks = PageLinks(
    headers = Headers.Builder().add(
        "link",
        "<https://api.unsplash.com/photos?page=1&per_page=20&per_page=20>; rel=\"first\", <https://api.unsplash.com/photos?page=1&per_page=20&per_page=20>; rel=\"prev\", <https://api.unsplash.com/photos?page=13760&per_page=20&per_page=20>; rel=\"last\", <https://api.unsplash.com/photos?page=3&per_page=20&per_page=20>; rel=\"next\""
    ).build()
)




