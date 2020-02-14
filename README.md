# Neomorph FrameLayout
[![](https://jitpack.io/v/4inodev/Neomorphic-FrameLayout-Android.svg)](https://jitpack.io/#4inodev/Neomorphic-FrameLayout-Android)

Neomorphic Design related Android library which is basically a custom FrameLayout.
Basically, you just put your TextViews and Button inside this FrameLayout and you're good to go!
 
<img src="https://raw.githubusercontent.com/4inodev/Neomorphic-FrameLayout-Android/master/screenshots/light_main.jpg" height="500"> <img src="https://raw.githubusercontent.com/4inodev/Neomorphic-FrameLayout-Android/master/screenshots/light_cp.jpg" height="500"><img src="https://raw.githubusercontent.com/4inodev/Neomorphic-FrameLayout-Android/master/screenshots/dark_main.jpg" height="500">
 
## Gradle Dependency

Add this in your root build.gradle file at the end of repositories:
```java
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
Add the dependency : 
```java
dependencies {
	        implementation 'com.github.4inodev:Neomorphic-FrameLayout-Android:1.0'
	}
```
Sync the gradle and that's it! :+1:


## Usage

### XML : 

```xml
<com.chinodev.androidneomorphframelayout.NeomorphFrameLayout  
  android:layout_width="match_parent"  
  android:layout_height="wrap_content"  
  app:neomorph_view_type="rectangular"  
  app:neomorph_shadow_type="inner"  
  app:neomorph_elevation="8dp"  
  app:neomorph_corner_radius="16dp"  
  app:neomorph_background_color="@color/neomorph_background_color"  
  app:neomorph_shadow_color="@color/neomorph_shadow_color"  
  app:neomorph_highlight_color="@color/neomorph_highlight_color">
```
## Attributes: 
```"neomorph_view_type"``` -  shape type: rectangular or circular
```"neomorph_shadow_type"``` - shadow type: inner or outer
```"neomorph_elevation"``` - dimension value, determines shadow elevation and inner padding
```"neomorph_corner_radius"``` - pretty self-explanatory =)
```"neomorph_background_color"``` - view color
```"neomorph_shadow_color"``` - right-bottom shadow color
```"neomorph_highlight_color"``` - left-top highlight color
```"neomorph_clickable"``` - not used yet

## LICENSE : 
```
MIT License

Copyright (c) 2017 GoodieBag

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
