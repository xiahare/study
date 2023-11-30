# p1: facet_id, such as ,
# p2: start_date, such as, "2023-11-18"
# p3: end_date (included), such as, "2023-11-19"
# sh rebuild_special_facet.sh 6481195661807435859 "2023-11-23" "2023-11-24"

# run a command in the background
nohup sh rebuild_special_facet.sh 989760109194424201 "2023-11-20" "2023-11-29" >> output.log &