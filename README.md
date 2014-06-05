# AppLover - Android

## Features
- Aks users if they like the app
- Users who like the app -> Ask them to rate the app in Google Play
- Users who dislike the app -> Ask them to send an e-mail to tell how to improve the app
- Very customizable
 - Custom style
 - Custom trigger logic
 - Hook for analytics

## Usage
A more extensive sample can be found here: https://github.com/tutti-ch/applover-android/tree/master/sample

build.gradle
```
compile 'ch.tutti.android.applover:library:1.0+'
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