--onBeforeTestCaseQuery

select 1 as data
select top 1 user_id from notes order by newid()

select '${__Context(testCaseData.escalation_type)}' as d