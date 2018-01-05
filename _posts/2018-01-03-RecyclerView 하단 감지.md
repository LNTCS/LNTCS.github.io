---
layout: post
title: 'RecyclerView 하단 감지'
date: 2018-01-03
author: LNTCS
tags: 잡끼
---

RecyclerView 에서 스크롤이 맨 마지막인지 확인하기
```kotlin
layoutManager.run {
	if(findFirstCompletelyVisibleItemPosition() + childCount >= itemCount){
		Log.i(TAG, "리스트 끝");
	 }
}
```
의 경우 정확하게 100의 스크롤 길이가 있을 때 위치 100이 아니라 마지막 아이템이 보이는 순간이다.

```kotlin
if(!recyclerView.canScrollVertically(1)){
	Log.i(TAG, "리스트 끝");
}
```
으로 하면 더 이상 스크롤이 되지 않는 상태일 때 `true`값을 반환


여담으로 `!recyclerView.canScrollVertically(-1)` 는 가장 위
<center>* * * * *</center>
