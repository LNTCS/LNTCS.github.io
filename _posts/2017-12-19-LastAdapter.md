---
layout: post
title: 'LastAdapter'
date: 2017-12-19
author: LNTCS
tags: 안드소전
---
## LastAdapter
기존에 RecyclerView 를 이용하기에는 각 리스트에 맞게 Adapter를 생성해주는 과정의 어려움이 컸다.
하지만 LastAdapter는 데이터바인딩을 기반으로 이를 쉽게 해주는 라이브러리로 수십줄로 따로 추가해야할 코드를 단 몇줄로 줄여준다.<br><br>

### 사용준비
사용에 앞서 언급했듯이 이 라이브러리는 DataBinding 이라는 친구가 기본이 되기 때문에 이전 글의 gradle 설정이 완료되어있어야 한다.

[LastAdapter Github](https://github.com/nitrico/LastAdapter) 
에서 상세한 내용을 살펴볼 수 있다.<br>

이 글을 작성하는 기준상 최신이 `2.3.0` 이기에<br>
`compile 'com.github.nitrico.lastadapter:lastadapter:2.3.0'` 구문을 dependencies 에 추가한다.

#### build.gradle (Module: app)
```gradle
apply plugin: 'kotlin-kapt'

android {
    ....
    dataBinding {
        enabled = true
    }
}

dependencies {
    kapt 'com.android.databinding:compiler:3.0.1' // gradle 버전
	....
    implementation 'com.github.nitrico.lastadapter:lastadapter:2.3.0'
    implementation 'com.android.support:appcompat-v7:27.0.2'
    implementation 'com.android.support:support-v4:27.0.2'
}
```
우선은 리스트에서 쓸 데이터 클래스를 만들어 준다.

#### User.kt
```kotlin
class User(var name: String, var email: String, var color: String){
    object CustomBindingAdapter{
        @BindingAdapter("android:background")
        @JvmStatic
        fun setBackgroundColor(layout: View?, colorCode: String){
            layout?.setBackgroundColor(Color.parseColor(colorCode))
        }
    }
}
```

이후 xml 파일에 리스트를 보여주기 위한 RecyclerView와 그 아이템의 레이아웃을 만들어 준다.

#### item_user.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <data>

        <variable
            name="item"
            type="com.example.lastadaptertest.User" />
    </data>

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="?attr/selectableItemBackground"
        android:clickable="true"
        android:gravity="center_vertical"
        android:orientation="horizontal">

        <View
            android:layout_width="8dp"
            android:layout_height="64dp"
            android:background="@{item.color}"
            tools:background="@color/colorAccent" />

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginLeft="16dp"
            android:orientation="vertical">

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="@{item.name}"
                android:textSize="18sp"
                android:textStyle="bold"
                tools:text="이름" />

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="@{item.email}"
                tools:text="ex@mple.com" />

        </LinearLayout>
    </LinearLayout>
</layout>
```
대충
![item_user](http://do-you-know-yuna.kim/assets/img/171219/item_user.png)
이렇게 생긴 리스트 아이템이다.<br><br>


이 리스트를 보여줄 곳에 RecyclerView를 추가해준다.

#### activity_main.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <android.support.v7.widget.RecyclerView
        android:id="@+id/mainRecycler"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:listitem="@layout/item_user" />
	<!-- tools:listitem 을 이용하여 IDE 오른쪽 미리보기 창에 리스트 아이템을 표시해준다. -->
</RelativeLayout>
```

이제 코틀린 코드 작성의 차례다.

#### MainActivity.kt
```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var uList = arrayListOf<User>(
                User("1번사람", "111@mail.com", "#f76363"),
                User("2번사람", "222@mail.com", "#ffbf00"),
                User("3번사람", "333@mail.com", "#a5d322"),
                User("4번사람", "444@mail.com", "#33b5e5"),
                User("5번사람", "555@mail.com", "#7e828e"),
                User("6번사람", "666@mail.com", "#e8cc35")
        )
		// 임의의 유저 데이터를 넣어 줬다.

        // 일반적인 세로 방향 리스트를 위해 LinearLayoutManager 이용
		mainRecycler.layoutManager = LinearLayoutManager(this)
		
        LastAdapter(uList, BR.item)
                .map<User>(R.layout.item_user)
                .into(mainRecycler)
    }
}
```
빰!

~~끝이다~~ 

----

----

----

### 다른 응용

#### 커스텀 핸들링
물론 모든 기능을 데이터 바인딩에 의존할 수는 없다.<br>
필요에 따라서 직접 기능을 넣어야 하는 때가 있는데 그럴 때에는 각 map구문에서 바인더를 받아서 재정의를 해준다.

```kotlin
/*
LastAdapter(uList, BR.item)
        .map<User>(R.layout.item_user)
        .into(mainRecycler)
핸들링이 추가되지 않은 이전 방식.
*/
		
LastAdapter(uList, BR.item)
        .map<User, ItemUserBinding>(R.layout.item_user){
            onClick {
                // 리스트 아이템이 클릭 되었을 때의 액션
                var position = it.adapterPosition
                // it.adapterPosition 으로 현재 리스트 포지션을 받아온다.
				
                var user = it.binding.item
                // it.binding.item 으로 눌린 리스트 아이템을 받아온다.
                
				uList.removeAt(position)
                mainRecycler.adapter.notifyItemRemoved(position)
            }
			onBind {
				// 리스트 아이템이 생성될 때의 액션
			}
			onLongClick { 
				// 리스트 아이템이 `길게` 클릭 되었을 때의 액션
			}
        }.into(mainRecycler)
```

----

#### 멀티타입 뷰

사실 리스트뷰 까지는 어려운 부분은 아니다.<br>
하지만 멀티타입에 들어서며 혼돈이 시작된다.
멀티타입 뷰란 하나의 리스트뷰에서 여러 디자인의 아이템 레이아웃을 쓰는 경우를 뜻 한다. <br><br>
예를 들면 채팅앱에서 일반적인 텍스트 아이템과 이미지나 이모티콘 등을 표시하는 아이템을 같은걸 쓸 수는 없다.<br>
텍스트 채팅에는 텍스트 레이아웃, 이미지에는 이미지 레이아웃 따로 제작을 하여 각 상황에 맞게 바꿔야 한다.<br><br>
물론 LastAdapter를 이용하면 간단히 표현 가능하다.

우선 커스텀 데이터를 하나 더 만들어주자.

#### Fruit.kt
```kotlin
class Fruit(var name: String, var color: Int)
```
간단히 이름과 색상값을 받는 데이터이다.

이 Fruit 이란 데이터랑 매치를 시켜줄 xml 파일도 추가

#### item_fruit.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <data>

        <variable
            name="item"
            type="com.example.lastadaptertest.Fruit" />
    </data>

    <LinearLayout
        android:id="@+id/itemFruitBg"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:gravity="center_vertical"
        android:orientation="horizontal">

        <TextView
            android:id="@+id/itemFruitName"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:gravity="center"
            android:padding="24dp"
            android:text="@{item.name}"
            android:textColor="@{item.color}"
            tools:text="이름" />

    </LinearLayout>
</layout>
```

이 두가지로 준비는 사실상 끝 kt 파일로 가자

#### MainActivity.kt
```kotlin
    var uList = arrayListOf<User>(~~~~)
```
모양으로 선언한 리스트의 자료형을 바꿔줄 것이다.
User 에서 Any 로
Any란 정말 모든 것이라고 생각을 하면 된다.
```kotlin
    var uList = arrayListOf<Any>(~~~~)
```
이렇게 선언을 해준다면 `uList` 에는 어떠한 값이던 들어갈 수 있다.
즉
```kotlin
    var uList = arrayListOf<Any>(
        User("1번사람", "111@mail.com", "#f76363"),
        User("2번사람", "222@mail.com", "#ffbf00"),
        Fruit("사과", Color.RED),
        User("3번사람", "333@mail.com", "#a5d322"),
        Fruit("수박", Color.GREEN),
        User("4번사람", "444@mail.com", "#33b5e5"),
        User("5번사람", "555@mail.com", "#7e828e"),
        Fruit("바나나", Color.YELLOW),
        User("6번사람", "666@mail.com", "#e8cc35"),
    	Fruit("블루베리", Color.BLUE)
	)
```
이런식으로 `Fruit`과 `User`형이 난잡하게 리스트에 들어갈 수 있다는 뜻이다.

이후 아래의 LastAdapter 의 설정 구문을 수정해준다.
```kotlin
        LastAdapter(uList, BR.item)
                .map<User, ItemUserBinding>(R.layout.item_user){
                    onClick {
                        // 해당 리스트를 클릭 했을 시의 액션

                        var position = it.adapterPosition
                        // it.adapterPosition 으로 현재 리스트 포지션을 받아온다.

                        var user = it.binding.item
                        // it.binding.item 으로 눌린 리스트 아이템을 받아온다.

                        uList.removeAt(position)
                        mainRecycler.adapter.notifyItemRemoved(position)
                    }
                }
                .map<Fruit, ItemFruitBinding>(R.layout.item_fruit){
                 onClick {
                     it.binding.item?.let { fruit ->
                         // it.binding.~~ id 이름으로 뷰를 바로 받아온다.
                         it.binding.itemFruitBg.setBackgroundColor(fruit.color)
                         it.binding.itemFruitName.setTextColor(Color.WHITE)
                     }
                 }
                }
                .into(mainRecycler)
```
간단하게 수정된 부분을 말해주자면 `.map` 구문이 추가 되었다.<br><br>
`.map<User>(R.layout.item_user)`<br>
`.map<Fruit>(R.layout.item_fruit)`<br>
두 개의 의미는 간단하게 `uList` 라는 리스트를 하나씩 돌면서 
아이템이 `User` 형이면 `R.layout.item_user`에 바인딩을 시켜서 보여주고, `Fruit` 형이라면 `R.layout.item_fruit)`에 바인딩을 시키겠다.<br>
라는 의미로 보면 된다. 

끝
<center>* * * * *</center>
