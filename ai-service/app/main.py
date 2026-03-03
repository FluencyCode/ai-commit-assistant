from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from typing import Optional
import os
from dotenv import load_dotenv

load_dotenv()

app = FastAPI(
    title="AI Commit Service",
    description="AI-powered Git commit message generator",
    version="1.0.0"
)

class CommitRequest(BaseModel):
    diff: str
    language: Optional[str] = "zh"  # zh or en

class CommitResponse(BaseModel):
    message: str
    success: bool

@app.get("/")
async def root():
    return {"message": "AI Commit Service is running", "version": "1.0.0"}

@app.post("/generate", response_model=CommitResponse)
async def generate_commit_message(request: CommitRequest):
    """
    Generate commit message from git diff
    """
    try:
        # TODO: 调用大模型 API 生成 commit message
        # 这里是占位符，后续会实现
        
        return CommitResponse(
            message="feat: initial commit",
            success=True
        )
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@app.get("/health")
async def health_check():
    return {"status": "healthy"}

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)
