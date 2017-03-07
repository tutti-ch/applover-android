# AppLover - Android
[![Release](https://jitpack.io/v/tutti-ch/applover-android.svg)](https://jitpack.io/#tutti-ch/applover-android)

![AppLover](https://raw.github.com/tutti-ch/applover-android/master/preview.png)

## Features
- Ask users if they like the app
- Users who like the app -> Ask them to rate the app in Google Play
- Users who dislike the app -> Ask them to send an e-mail to tell how to improve the app
- Very customizable
 - Custom style
 - Custom trigger logic
 - Hook for analytics

## Usage

Add this in your root `build.gradle` file (**not** your module `build.gradle` file):

```gradle
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```

```gradle
dependencies {
    ...
    compile 'com.github.tutti-ch:applover-android:v2.1'
}
```

MainActivity.java:
``` java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppLover appLover = AppLover.get(this);
        appLover.setFeedbackEmail("support@yourapp.com");
        appLover.monitorLaunch(this);
        appLover.showDialogIfConditionsMet(this);
    }
```

## License

    Copyright (c) 2014 tutti.ch AG

    Permission to use, copy, modify, and distribute this software for any
    purpose with or without fee is hereby granted, provided that the above
    copyright notice and this permission notice appear in all copies.

    THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
    WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
    MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
    ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
    WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
    ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
    OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.