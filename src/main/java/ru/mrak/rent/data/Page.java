package ru.mrak.rent.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Page<T> {
    private List<T> content;
    private Integer page;
    private Integer pageSize;
    private Long count;
}
