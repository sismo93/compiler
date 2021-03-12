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
store i32 4, i32* %a_0
%b_0 = alloca i32
%1 = load i32, i32* %a_0
store i32 %1, i32* %b_0
%2 = load i32, i32* %a_0
%3 = icmp eq i32 %2, 4
br i1 %3, label %ifCode3, label %elseCode3
br label %ifCode3
ifCode3:
%5 = load i32, i32* %b_0
%6 = icmp eq i32 %5, 1
br i1 %6, label %ifCode6, label %elseCode6
br label %ifCode6
ifCode6:
br label %restCode6
br label %elseCode6
elseCode6:
%9 = load i32, i32* %b_0
%10 = icmp eq i32 %9, 4
br i1 %10, label %ifCode10, label %elseCode10
br label %ifCode10
ifCode10:
%c_0 = alloca i32
store i32 10, i32* %c_0
%12 = load i32, i32* %c_0
%13 = icmp sgt i32 %12, 0
br i1 %13, label %ifCode13, label %elseCode13
br label %ifCode13
ifCode13:
%15 = load i32, i32* %c_0
call void @println(i32 %15)
br label %restCode13
br label %elseCode13
elseCode13:
br label %restCode13
restCode13:
br label %restCode10
br label %elseCode10
elseCode10:
br label %restCode10
restCode10:
br label %restCode6
restCode6:
br label %restCode3
br label %elseCode3
elseCode3:
%19 = load i32, i32* %a_0
%20 = icmp slt i32 %19, 1
br i1 %20, label %ifCode20, label %elseCode20
br label %ifCode20
ifCode20:
%22 = load i32, i32* %a_0
%23 = icmp eq i32 %22, 9
br i1 %23, label %ifCode23, label %elseCode23
br label %ifCode23
ifCode23:
%25 = load i32, i32* %a_0
call void @println(i32 %25)
br label %restCode23
br label %elseCode23
elseCode23:
%27 = load i32, i32* %b_0
call void @println(i32 %27)
br label %restCode23
restCode23:
br label %restCode20
br label %elseCode20
elseCode20:
%29 = load i32, i32* %a_0
call void @println(i32 %29)
br label %restCode20
restCode20:
br label %restCode3
restCode3:
ret void
}

define i32 @main(){
call void @defaultFunctionName0()
ret i32 0
}
