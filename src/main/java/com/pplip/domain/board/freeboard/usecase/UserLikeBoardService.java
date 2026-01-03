package com.pplip.domain.board.freeboard.usecase;

import com.pplip.domain.board.freeboard.api.response.FreeBoardResponse;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserLikeBoardService {
    FreeBoardResponse.BoardLike getLikeFreeBoard(Long id, UserDetails userDetails);

    FreeBoardResponse.BoardLike likeFreeBoard(Long id, UserDetails userDetails);

    FreeBoardResponse.BoardLike unlikeFreeBoard(Long id, UserDetails userDetails);
}
