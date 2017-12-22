package com.example.calendar

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import com.github.nitrico.lastadapter.LastAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class CData(var date: Int)
class MainActivity : AppCompatActivity() {

    // 전체 달력 데이터가 들어갈 리스트
    var cList = ArrayList<CData>()

    //  월 단위의 값을 가진 배열
    var mArr = arrayOf(Calendar.JANUARY, Calendar.FEBRUARY, Calendar.MARCH,
            Calendar.APRIL, Calendar.MAY, Calendar.JUNE, Calendar.JULY, Calendar.AUGUST,
            Calendar.SEPTEMBER, Calendar.OCTOBER, Calendar.NOVEMBER, Calendar.DECEMBER)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setCalendar()

        //  7칸짜리 그리드 리사이클러
        mainRecycler.layoutManager = GridLayoutManager(this, 7)
        LastAdapter(cList, BR.item)
                .map<CData>(R.layout.item_calendar)
                .into(mainRecycler)
    }

    private fun setCalendar() {
        cList.clear()

        //  2017년 12월 1일로 기준날짜를 세팅
        var cal = Calendar.getInstance()
        cal.set(2017, mArr[11], 1)

        //  12월 1일이 무슨 요일인지 받아온다.
        var index = cal.get(Calendar.DAY_OF_WEEK) - 1

        //  해당요일 전까지 0 이라는 데이터를 넣어준다.
        //  12월 1일이 금요일이므로 일, 월, 화, 수, 목 의 수만큼의 더미 데이터
        (1..index).forEach {
            cList.add(CData(0))
        }

        //  해당 월의 최대 날짜수를 구한 뒤 그 개수만큼 데이터 입력 1 ~ 28/30/31
        (1..cal.getActualMaximum(Calendar.DAY_OF_MONTH)).forEach {
            cList.add(CData(it))
        }

        //  뒤의 빈칸을 채워준다.
        //  달력의 최대치는 6주 즉 42일이다.
        (cList.size..41).forEach {
            cList.add(CData(0))
        }
    }
}
