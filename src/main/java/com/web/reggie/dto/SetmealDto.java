package com.web.reggie.dto;

import com.web.reggie.entity.Setmeal;
import com.web.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
