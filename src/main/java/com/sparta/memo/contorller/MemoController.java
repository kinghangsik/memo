package com.sparta.memo.contorller;

import com.sparta.memo.dto.MemoRequestDto;
import com.sparta.memo.dto.MemoResponseDto;
import com.sparta.memo.entity.Memo;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MemoController {
    private final Map<Long, Memo> memoList = new HashMap<>();

    //create 기능
    @PostMapping("/memos")
    public MemoResponseDto createMemo(@RequestBody MemoRequestDto requestDto) {
        // RequestDto -> entity
        Memo memo = new Memo(requestDto);

        // Memo Max ID check == ID값으로 Memo구분(중복 x) 현재 ID의 가장 마지막 +1
        Long maxId = memoList.size() > 0 ? Collections.max(memoList.keySet()) + 1 : 1;
        memo.setId(maxId);

        // DB저장
        memoList.put(memo.getId(), memo);

        // entity -> responseDto
        MemoResponseDto memoResponseDto = new MemoResponseDto(memo);

        return memoResponseDto;
    }
    //read 기능
    @GetMapping("/memos")
    public List<MemoResponseDto> getMemos() {
        //Map to List
        // .values().stream() == values()의 값들을 하나씩 for문처럼 돌려준다.
        List<MemoResponseDto> responseList = memoList.values().stream()
                .map(MemoResponseDto::new).toList();

        return responseList;
    }

    //update 기능
    @PutMapping("/memos/{id}")
    public Long updateMemo(@PathVariable Long id, @RequestBody MemoRequestDto requestDto) {
        //해당 메모가 DB에 존재하는지 확인
        if(memoList.containsKey(id)) {
            //해당 메모 가져오기
            Memo memo = memoList.get(id);

            //메모 수정
            memo.update(requestDto);
            return memo.getId();
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }

    //delete 기능
    @DeleteMapping("/memos/{id}")
    public Long deleteMemo(@PathVariable Long id) {
        //해당 메모가 DB에 존재하는지 확인
        if(memoList.containsKey(id)) {
            //해당 메모 삭제하기
            memoList.remove(id);
            return id;
        } else {
            throw new RuntimeException("선택한 메모는 존재하지 않습니다.");
        }
    }


}