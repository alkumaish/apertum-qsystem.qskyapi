SELECT pager_results.ip, pager_results.event_time, pager_results.qsys_version,  pager_results.input_data, pager_quiz_items.item_text, pager_data.text_data
FROM pager_results 
left join pager_data on pager_data.id =  pager_results.pager_data_id
left join pager_quiz_items on pager_quiz_items.id =  pager_results.quiz_id
where !isnull( pager_data.text_data)
order by pager_results.event_time desc;