@.strR = private unnamed_addr constant [3 x i8] c"%d\00", align 1
define i32 @readInt() {
  %x = alloca i32, align 4
  %1 = call i32 (i8*, ...) @__isoc99_scanf(i8* getelementptr inbounds ([3 x i8], [3 x i8]* @.strR, i32 0, i32 0), i32* %x)
  %2 = load i32, i32* %x, align 4
  ret i32 %2
}
declare i32 @__isoc99_scanf(i8*, ...)
@.strP = private unnamed_addr constant [4 x i8] c"%d\0A\00", align 1
define void @println(i32 %x) {
  %1 = alloca i32, align 4
  store i32 %x, i32* %1, align 4
  %2 = load i32, i32* %1, align 4
  %3 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.strP, i32 0, i32 0), i32 %2)
  ret void
}
declare i32 @printf(i8*, ...)
define void @defaultFunctionName0(){
%a_0 = alloca i32
store i32 2, i32* %a_0
%1 = load i32, i32* %a_0
%2 = icmp eq i32 %1, 0
br i1 %2, label %ifCode2, label %elseCode2
br label %ifCode2
ifCode2:
br label %restCode2
br label %elseCode2
elseCode2:
%5 = load i32, i32* %a_0
%6 = icmp eq i32 %5, 1
br i1 %6, label %ifCode6, label %elseCode6
br label %ifCode6
ifCode6:
br label %restCode6
br label %elseCode6
elseCode6:
%9 = load i32, i32* %a_0
%10 = icmp eq i32 %9, 2
br i1 %10, label %ifCode10, label %elseCode10
br label %ifCode10
ifCode10:
%12 = load i32, i32* %a_0
call void @println(i32 %12)
br label %restCode10
br label %elseCode10
elseCode10:
br label %restCode10
restCode10:
br label %restCode6
restCode6:
br label %restCode2
restCode2:
ret void
}

define i32 @main(){
call void @defaultFunctionName0()
ret i32 0
}
