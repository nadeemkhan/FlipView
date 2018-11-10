# FlipView
You can create views with two side in your android project


# Demo
![](https://github.com/denzsbs/FlipView/blob/master/art/flipview1.gif)

# with recyclerview 
![](https://github.com/denzsbs/FlipView/blob/master/art/flipview2.gif)

# Usage
```xml
    <com.denzsbs.flipviewlib.FlipView
        android:id="@+id/flipView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:flipDuration="400"
        app:flipEnabled="true"
        app:flipFrom="right"
        app:flipOnTouch="false"
        app:flipType="horizontal">
       
       <!--first back layout -->
    

       <!-- front layout here -->
       
    </com.denzsbs.flipviewlib.FlipView>
 ```
    
## Attributes

|  Attribute  |  Description |
| ------------ | ------------ |
| flipDuration |  The duration of flip animation in milliseconds. |
| flipOnTouch   |  View should be flipped on touch or not. |
| flipType  |   View should flip in vertical or horizontal |
| flipFrom |  View should flip from left to right Or right to left(Horizontal type) or car should flip to front or back(Vertical type) |

## Installation

Add it in your root build.gradle :
```gradle
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
Add the dependency
```gradle
	dependencies {
	      implementation 'com.github.denzsbs:FlipView:1.0.0'
	}
```
     
     
License
--------
    Copyright 2018 Deniz.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


