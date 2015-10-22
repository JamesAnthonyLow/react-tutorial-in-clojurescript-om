require 'sinatra'
require 'json'

get "/comments.json" do
  content_type :json 
  File.read("public/comments.json")
end
