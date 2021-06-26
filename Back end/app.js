//jshint esversion:6
const express = require("express");
const bodyParser = require("body-parser");
const ejs = require("ejs");
const mongoose = require('mongoose');
const app = express();
const path=require("path")
const querystring=require("querystring");
const url=require("url");
app.set('view engine', 'ejs');
app.use(bodyParser.urlencoded({extended: true}));
app.use(express.static("public"));
mongoose.connect(process.env.API_KEY,{useNewUrlParser:true,useUnifiedTopology: true});
//mongoose.connect("mongodb://localhost:27017/ifpDB",{useNewUrlParser:true,useUnifiedTopology: true});
const articleSchema=new mongoose.Schema({
  title:String,
  content:String
})
const Article=mongoose.model("Article",articleSchema)

app.get("/",function(req,res){
  Article.find(function(err,foundArticles){
    if (!err) {
      res.send(foundArticles);
    } else {
      res.send(err);
    }
  });
})
app.post("/",function(req,res){

    const article1=new Article({
      title:req.body.title || req.query.title,
      content:req.body.content || req.query.content
    });



  article1.save(function(err){
    if(!err){
      res.send("Post successfull");
    }
    else{
      res.send(err);
    }
  });
})
app.delete("/",function(req,res){
  Article.deleteMany(function(err){
    if (!err) {
      res.send("Deleted successfully");
    } else {
      res.send(err);
    }
  });
});
// Parameter
app.route("/:articleName")
.get(function(req,res){
  Article.findOne({title:req.params.articleName},function(err,foundArticle){

    if (foundArticle) {
      res.send(foundArticle);
    } else {
      res.send("Not found");
    }
  });
})
.put(function(req,res){
  Article.update({title:req.params.articleName},{title:req.body.title || req.query.title,content:req.body.content || req.query.content},{overwrite:true},
  function(err){
    if(!err){
      res.send("Successfully updated article")
    }
  });
})
.patch(function(req,res){
  Article.update({title:req.params.articleName},{$set:req.body},
  function(err){
    if(!err){
      res.send("Successfully updated articles");
    }
    else{
      res.send(err);
    }
  });
})
.delete(function(req,res){
  Article.deleteOne({title:req.params.articleName},function(err){
    if (!err) {
      res.send("Deleted successfully");
    } else {
      res.send(err);
    }
  });
});

let port=process.env.PORT;
if (port==null||port=="") {
  port=3000;
}

app.listen(port, function() {
  console.log("Server started on port successfully");
});
