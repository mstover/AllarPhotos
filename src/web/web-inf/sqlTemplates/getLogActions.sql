select log_values.val STRING from log inner join log_names on log.category_id=log_names.category_id
inner join log_values on log.value_id=log_values.value_id where log_names.val='action' group by log_values.val