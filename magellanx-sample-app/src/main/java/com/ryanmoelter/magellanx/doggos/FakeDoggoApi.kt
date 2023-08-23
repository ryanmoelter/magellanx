package com.ryanmoelter.magellanx.doggos

import kotlinx.coroutines.delay

class FakeDoggoApi : DoggoApi {

  override suspend fun getRandomDoggoImage(): DoggoImageResponse {
    delay(500)
    return DoggoImageResponse(
      "https://images.dog.ceo/breeds/poodle-standard/n02113799_7258.jpg",
      "success"
    )
  }

  override suspend fun getRandomDoggoImageByBreed(breed: String): DoggoImageResponse {
    delay(500)
    return DoggoImageResponse(
      "https://images.dog.ceo/breeds/poodle-standard/n02113799_7258.jpg",
      "success"
    )
  }

  override suspend fun getBreeds(): BreedListResponse {
    delay(500)
    return BreedListResponse(
      breeds = listOf(
        "affenpinscher",
        "african",
        "airedale",
        "akita",
        "appenzeller",
        "australian",
        "basenji",
        "beagle",
        "bluetick",
        "borzoi",
        "bouvier",
        "boxer",
        "brabancon",
        "briard",
        "buhund",
        "bulldog",
        "bullterrier",
        "cattledog",
        "chihuahua",
        "chow",
        "clumber",
        "cockapoo",
        "collie",
        "coonhound",
        "corgi",
        "cotondetulear",
        "dachshund",
        "dalmatian",
        "dane",
        "deerhound",
        "dhole",
        "dingo",
        "doberman",
        "elkhound",
        "entlebucher",
        "eskimo",
        "finnish",
        "frise",
        "germanshepherd",
        "greyhound",
        "groenendael",
        "havanese",
        "hound",
        "husky",
        "keeshond",
        "kelpie",
        "komondor",
        "kuvasz",
        "labradoodle",
        "labrador",
        "leonberg",
        "lhasa",
        "malamute",
        "malinois",
        "maltese",
        "mastiff",
        "mexicanhairless",
        "mix",
        "mountain",
        "newfoundland",
        "otterhound",
        "ovcharka",
        "papillon",
        "pekinese",
        "pembroke",
        "pinscher",
        "pitbull",
        "pointer",
        "pomeranian",
        "poodle",
        "pug",
        "puggle",
        "pyrenees",
        "redbone",
        "retriever",
        "ridgeback",
        "rottweiler",
        "saluki",
        "samoyed",
        "schipperke",
        "schnauzer",
        "segugio",
        "setter",
        "sharpei",
        "sheepdog",
        "shiba",
        "shihtzu",
        "spaniel",
        "spitz",
        "springer",
        "stbernard",
        "terrier",
        "tervuren",
        "vizsla",
        "waterdog",
        "weimaraner",
        "whippet",
        "wolfhound"
      ),
      "success"
    )
  }
}
