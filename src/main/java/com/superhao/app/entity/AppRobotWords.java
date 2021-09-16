package com.superhao.app.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 
 * @author superhao
 * @email 928649522@qq.com
 * @date 2019-11-27 13:58:11
 */
@Data
@TableName("app_robot_words_t")
@ToString
@Getter
@Setter
public class AppRobotWords implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	@TableId(value = "words_id", type = IdType.AUTO)
private Long wordsId;
	/**
	 * 
	 */
	@TableField(value = "wkey")
private String wkey;
	/**
	 * 
	 */
	@TableField(value = "wvalue")
private String wvalue;
	/**
	 * 
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	@TableField(value = "dictionary_id")
private Long dictionaryId;

/**
	AppRobotWords.wordsId
	AppRobotWords.key
	AppRobotWords.value
	AppRobotWords.dictionaryId
 */
}
