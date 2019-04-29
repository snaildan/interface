package cn.gmw.api.meiyou.utils;

import cn.gmw.api.meiyou.entity.APIWorkReport;
import cn.gmw.api.meiyou.entity.ReleaseLib;
import cn.gmw.api.meiyou.entity.WorkItem;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DeduplUtil {

	/**
	 * @author 0-Vector
	 * @date 2017年2月8日上午10:20:25
	 * @param list
	 * @param workReport
	 * @return
	 */
	public static List<ReleaseLib> distinct(List<ReleaseLib> list, float similarity, APIWorkReport workReport) {
		List<ReleaseLib> reList = new ArrayList<ReleaseLib>();
		for (int i = 0; i < list.size(); i++) {
			boolean isDuplicated = false;
			for (int j = i + 1; j < list.size(); j++) {
				float s = LevenshteinDistance.getLevenshteinDistance(list.get(i).getTitle(), list
						.get(j).getTitle());
				if (s >= similarity) {
					log.info("---- 过滤文档相似度过高的新闻 ----ID："
							+ list.get(i).getArticleId() + "|【"
							+ list.get(i).getTitle() + "】");
					log.info("---- ID：" + list.get(j).getArticleId() + "|【"
							+ list.get(j).getTitle() + "】的相似度为：" + s * 100
							+ "%");
					isDuplicated = true;
					{
						WorkItem workItem = new WorkItem();
						workItem.setId(String.valueOf(list.get(i)
								.getArticleId()));
						workItem.setSuccess(false);
						workItem.setComment("重复稿件");
						workItem.setPubTime(list.get(i).getPubTime());
						workItem.setTitle(list.get(i).getTitle());
						workItem.setUrl(list.get(i).getArtUrl());
						workReport.getWorkItemList().add(workItem);
					}
					break;
				}
			}
			if (!isDuplicated) {
				reList.add(list.get(i));
			}
		}
		return reList;
	}
}
