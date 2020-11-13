// Possible case class structure
// Built at https://json2caseclass.cleverapps.io/

case class Trends(
  name: String,
  url: String,
  promoted_content: String,
  query: String,
  tweet_volume: Double
)

case class Locations(
  name: String,
  woeid: Double
)

case class R00tJsonObject(
  trends: List[Trends],
  as_of: String,
  created_at: String,
  locations: List[Locations]
)
