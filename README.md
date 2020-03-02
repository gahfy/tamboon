[![Build Status](https://travis-ci.org/gahfy/tamboon.svg?branch=master)](https://travis-ci.org/gahfy/tamboon)

# Configuration

Update the following code to `app/omise.gradle`:

```
omise_public_key="\"\""
omise_password="\"\""
```

And change the following code in `app/src/main/java/com/omise/tamboon/network/TamboonApi.kt`

```
const val TAMBON_BASE_URL: String = "http://10.0.2.2:8080/"
```

With the url of your server
