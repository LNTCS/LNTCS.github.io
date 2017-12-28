package com.example.osunview

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addNote(Note(NoteType.QUARTER, 0)) // 미에 4분음표
        addNote(Note(NoteType.EIGHTH, 2)) // 솔에 8분음표
        addNote(Note(NoteType.QUARTER_REV, 5)) // ^도에 4분음표 // 5 (시) 이상으로는 _REV 음표
        addNote(Note(NoteType.EIGHTH_REV, 6)) // ^레에 8분음표 // 5 (시) 이상으로는 _REV 음표
        addNote(Note(NoteType.LINE)) // 마디 나누는 선
        addNote(Note(NoteType.QUARTER_REV, 7)) // ^미에 4분음표 // 5 (시) 이상으로는 _REV 음표
        addNote(Note(NoteType.EIGHTH_REV, 5)) // 시에 8분음표 // 5 (시) 이상으로는 _REV 음표
        addNote(Note(NoteType.EIGHTH, 4)) // 라에 8분음표
        addNote(Note(NoteType.QUARTER, 1)) // 파에 4분음표
        addNote(Note(NoteType.LINE)) // 마디 나누는 선
    }
/*
 TODO line:71 에 보면 lines[pos / 2 +1] 인데 현재 오선지의 범위를 벗어나는 경우는 개발이 안되어있음
 가상의 투명한 선을 만들어서 아마 추가 해줘야 할듯 현재 미~^미 까지의 범위만 표현 가능
  */

    var id = 1

    private fun addNote(note: Note) {
        var lines = arrayOf(R.id.line5, R.id.line4, R.id.line3, R.id.line2, R.id.line1)
        when(note.type){
            NoteType.LINE ->{
                var height = RelativeLayout.LayoutParams.MATCH_PARENT
                var width = resources.getDimensionPixelSize(R.dimen.osun_line_border)

                var lineView = View(this) // 새로운 View 생성
                var params = RelativeLayout.LayoutParams(width, height) // 가로세로 값 입력

                lineView.id = id++
                if (lineView.id != 1) { // 첫 노트는 오른쪽 옵션이 필요없다.
                    params.addRule(RelativeLayout.RIGHT_OF, lineView.id - 1)
                    // 이전 id 값의 오른쪽
                }
                params.addRule(RelativeLayout.ALIGN_TOP, lines[4])
                params.addRule(RelativeLayout.ALIGN_BOTTOM, lines[0])
                // 위는 맨 윗줄에 맞추고 아래는 맨 아랫줄에 맞춤 (오선지의 전체 사이즈)
                lineView.layoutParams = params // 속성들 입력
                lineView.setBackgroundColor(Color.parseColor("#343434")) // 색 씌우고

                osunLay.addView(lineView) // 선 추가
            }
            else ->{ // 선이 아니 나머지 (음표)
                var margin = resources.getDimensionPixelSize(R.dimen.osun_margin)
                var height = resources.getDimensionPixelSize(R.dimen.note_height)
                var width = resources.getDimensionPixelSize(R.dimen.note_width)

                var noteView = ImageView(this) // 새로운 이미지뷰 생성
                var params = RelativeLayout.LayoutParams(width, height) // 가로세로 값 입력
                noteView.id = id++
                if (noteView.id != 1) { // 첫 노트는 오른쪽 옵션이 필요없다.
                    params.addRule(RelativeLayout.RIGHT_OF, noteView.id - 1)
                    // 이전 id 값의 오른쪽
                }
                if (note.position >= 5) { // 거꾸로된 음표
                    params.addRule(RelativeLayout.ALIGN_TOP, lines[note.position / 2 + 1])
                    if (note.position % 2 == 0) { // 0이나 짝수일 경우 줄에 걸치므로 마진을 넣는다.
                        params.topMargin = margin
                    }
                } else { // 정방향
                    params.addRule(RelativeLayout.ALIGN_BOTTOM, lines[note.position / 2])
                    if (note.position % 2 == 0) { // 0이나 짝수일 경우 줄에 걸치므로 마진을 넣는다.
                        params.bottomMargin = -1 * margin
                    }
                }
                noteView.layoutParams = params // 속성들 입력
                noteView.setImageResource(getNoteImg(note.type)) // 이미지 씌우고

                osunLay.addView(noteView) // 이미지 추가
            }
        }
    }

    class Note(var type: NoteType, var position: Int = 0) // 미 (가장 아랫줄에 걸치는 음표)를 기준으로 0
    enum class NoteType {
        QUARTER,
        QUARTER_REV,
        EIGHTH,
        EIGHTH_REV,
        LINE
    }

    fun getNoteImg(type: NoteType) = when (type) {
        NoteType.QUARTER -> R.drawable.quarter
        NoteType.QUARTER_REV -> R.drawable.quarter_rev
        NoteType.EIGHTH -> R.drawable.eighth
        NoteType.EIGHTH_REV -> R.drawable.eighth_rev
        else -> R.drawable.quarter
    }
}
