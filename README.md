Classic Material TimePicker
===========

Simple, customisable TimePicker created in the "old" Android API 16 style.

Restyled with **Material 2.0** guidelines and powered by **RecyclerView.**

| **Classic Material TimePicker**|**"Old" Android Api 16 TimePicker**|
| ------------- |:-------------:|
|<img src="assets/demo.gif" width="250" alt="demo">|<img src="assets/screenshot_old_picker.png" width="250" alt="screenshot 1">

**High Resolution Demo:**

https://giant.gfycat.com/LastingImpassionedGelada.webm

## Setup
This library is being distributed via [JitPack](http://jitpack.io/)

[AndroidX](https://developer.android.com/jetpack/androidx) libraries are required.

**Step 1:**  
Add Jitpack repository in root **build.gradle** at the end of repositories:
```groovy
allprojects {
  repositories {
   maven { url 'https://jitpack.io' }
  }
}
```

**Step 2:**  
Add dependency in app project:
```groovy
dependencies {
  implementation 'com.github.michaldrabik:classicmaterialtimepicker:1.0.0'
}
```

## Usage
See [Sample App](https://github.com/michaldrabik/classicmaterialtimepicker/blob/master/app/src/main/java/com/michaldrabik/cmtpsample/MainActivity.kt) for full example.

Create and show time picker's [CmtpDialogFragment](https://github.com/michaldrabik/classicmaterialtimepicker/blob/master/library/src/main/java/com/michaldrabik/classicmaterialtimepicker/CmtpDialogFragment.kt):
```kotlin
val timePicker = CmtpDialogFragment.newInstance()
timePicker.show(supportFragmentManager, "TimePickerTag")
```
Set initial time with `setInitialTime12()` or `setInitialTime24()` method:
```kotlin
val timePicker = CmtpDialogFragment.newInstance()

// Set initial time and initialise time picker in 12-hour time format.
timePicker.setInitialTime12(5, 15, PM)

// Set initial time and initialise time picker in 24-hour time format.
timePicker.setInitialTime24(23, 30)

timePicker.show(supportFragmentManager, "TimePickerTag")
```

Selected time can be retrieved with one of the listeners:
```kotlin
val timePicker = CmtpDialogFragment.newInstance()

// Set 12-hour time format listener.
// Use this listener if time picker was initialised in 12-hour format.
timePicker.setOnTime12PickedListener { time12 ->
  // Do something with picked time.
})

// Set 24-hour time format listener.
// Use this listener if time picker was initialised in 24-hour format.
timePicker.setOnTime24PickedListener { time24 ->
  // Do something with picked time.
})

timePicker.show(supportFragmentManager, "TimePickerTag")
```

[CmtpDialogFragment](https://github.com/michaldrabik/classicmaterialtimepicker/blob/master/library/src/main/java/com/michaldrabik/classicmaterialtimepicker/CmtpDialogFragment.kt) is being used as a host for [CmtpTimePickerView](https://github.com/michaldrabik/classicmaterialtimepicker/blob/master/library/src/main/java/com/michaldrabik/classicmaterialtimepicker/CmtpTimePickerView.kt) view.

You can use this view by itself or host it in a different Android component (like [Bottom Sheet](https://developer.android.com/reference/android/support/design/widget/BottomSheetDialogFragment))

## Customization
Each element of the time picker can be customized:

**Dialog background**

Override following style in your app with your own background drawable:
```xml
<style name="CmtpDialogFrameStyle" parent="CmtpDialogTheme">
  <item name="android:windowBackground">@drawable/your_background_drawable</item>
</style>
```

**Dialog positive/negative button text**

Provide custom texts while creating DialogFragment's instance:
```kotlin
val timePicker = CmtpDialogFragment.newInstance("Pick Time", "Hide")
timePicker.show(supportFragmentManager, "TimePickerTag")
```

**Dialog positive button style**

Override following style in your app with your own text style:
```xml
<style name="CmtpDialogPositiveButtonStyle" parent="Widget.AppCompat.Button.ButtonBar.AlertDialog">
  <item name="android:textColor">#FF0000</item>
  <!-- other properties -->
</style>
```

**Dialog negative button style**

Override following style in your app with your own text style:
```xml
<style name="CmtpDialogNegativeButtonStyle" parent="Widget.AppCompat.Button.ButtonBar.AlertDialog">
  <item name="android:textColor">#FF0000</item>
  <!-- other properties -->
</style>
```

**Custom text frame drawable, text color and size**

Override following style in your app with your own text style:
```xml
<style name="CmtpViewTheme">
  <item name="cmtpTextFrameDrawable">@drawable/custom_text_frame</item>
  <item name="cmtpTextColor">#000000</item>
  <item name="cmtpTextSize">20sp</item>
</style>
```

See [Sample App styles](https://github.com/michaldrabik/classicmaterialtimepicker/blob/master/app/src/main/res/values/styles.xml) for an example of full customization.

## Release Notes
1.0.0 - Initial Release

## License

**Copyright 2019 Michal Drabik**

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.  
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
See the GNU General Public License for more details.  
You should have received a copy of the GNU General Public License along with this program.   
If not, see [http://www.gnu.org/licenses/](http://www.gnu.org/licenses/).
