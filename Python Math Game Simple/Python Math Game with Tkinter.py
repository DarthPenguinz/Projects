from tkinter import *
import random
from timeit import default_timer as timer


##### functions for game 

#convert number and check 
def anschecker(answer,input):
    sans = answer.replace("-","")
    splittedans = sans.split('.')
    if len(splittedans) <= 2:

        for eachside in splittedans:
            if eachside.isnumeric():
                y = float(answer)
                if y == input:
                    #returns (IF ans correct=0)
                    return 0
                else:
                    #returns 1 if wrong answer
                    return 1   
            else:
                #return 2 if wrong type of answer is input
                return 2
    else:
        return 2


#question conver to answer

#generate lvl1 qs
#return str,ans
def level1qs(num1,num2):
    x = random.randint(1,2)
    a = random.randint(num1,num2)
    b = random.randint(num1,num2)

    questions = {1:"{} + {}".format(a,b),2:"{} - {}".format(a,b)}
    answers = {1:round(a+b,2),2:round(a-b,2)}
    return questions[x],answers[x]

#generate lvl2 qs
def level2qs(num1,num2):
    x = random.randint(1,4)
    a = random.randint(num1,num2)
    b = random.randint(num1,num2)
    c = random.randint(num1,num2)

    questions = {1:"{} + {} + {}".format(a,b,c),2:"{} - {} - {}".format(a,b,c),3:"{} * {}".format(a,b),4:"{} / {}".format(a,b)}
    answers = {1: round(a+b+c,2),2:round(a-b-c,2),3:round(a*b,2),4:round(a/b,2)}
    return questions[x],answers[x]

#generate lvl3 qs
def level3qs(num1,num2):
    x = random.randint(1,4)
    a = random.randint(num1,num2)
    b = random.randint(num1,num2)

    questions = {1:"({} + {}) * 5".format(a,b),2:"{}^2 + (3 * {})".format(a,b),3:"({} + {})^3".format(a,b),4:"({} / {})^2".format(a,b)}
    answers = {1:round((a+b)*5,2),2:round(a**2+(3*b),2),3:round((a+b**2),2),4:round((a/b)**2,2)}
    return questions[x],answers[x]

#add more levels next time




#Root Def
root = Tk()


#Game Functions

#Start Page 
def intro():
    startlabel.pack()
    startbutton.pack()

#Instructions Page
def instructions():
    Text.pack()
    Begin.pack()
    startbutton.pack_forget()
    startlabel.pack_forget()

#check if answer correct 
def checkans():
    try:
        # print(round(float(answer.get()),2),returnans(),round(float(ans.get()),2),type(returnans()))
        if round(float(answer.get()),2) == returnans(): #if ans correct
            score.set(score.get()+200)    #award points
            if score.get() >3000:     #check how many points to set level
                endgame()
            elif score.get() > 2000:
                level.set(3)
            elif score.get() > 1000:
                level.set(2)

            if level.get() == 1:                #chcek level to set question difficulty
                update(level1qs,1,20)
            elif level.get()==2:
                update(level2qs,1,20)
            else: 
                update(level3qs,1,20)
            
        else:
            if score.get() >0: 
                score.set(score.get()-100)   #dedduct points if wrong minimum 0 points
            tryagain()
    except:
        inputwrongtype()
        
#set ans to ans which 
def setans(newans):
    newansstr = str(newans)
    ans.set(newansstr)

#return ans in float type
def returnans():
    return float(ans.get())

#update question and answer values
def update(f,n1,n2):
    q,nans = f(n1,n2)
    quesmain.set(q)
    print(nans,type(nans))
    setans(nans)
    answer.delete(first = 0 , last = 100)
    textabovebox.set("Good Job! Enter the next answer Below")
        
#wrong answer
def tryagain():
    answer.delete(first=0,last=100)
    textabovebox.set("Wrong Answer, Please Try again")

#wrong input
def inputwrongtype():
    answer.delete(first=0,last=100)
    textabovebox.set("Answer is not of correct format, Please try again")

#display level 
def displaylevel():
    Text.pack_forget() 
    Begin.pack_forget()
    qsword.pack()
    qslabel.pack()
    wordans.pack()
    answer.pack()
    submit.pack()
    hintbutton.pack()
    scorewordlabel.place(x=200,y=0)
    scorelabel.place(x=250,y=0)
    levelwordlabel.place(x=200,y=20)
    levellabel.place(x=250,y=20)

#display end game screen
def endgame():
    qsword.pack_forget()
    qslabel.pack_forget()
    wordans.pack_forget()
    answer.pack_forget()
    submit.pack_forget()
    scorewordlabel.place_forget()
    scorelabel.place_forget()
    levelwordlabel.place_forget()
    levellabel.place_forget()
    hintbutton.pack_forget()
    endgamelabel.pack()
    restartbutton.pack()
    endgamebutton.pack()

#restart game
def playagain():
    q = ""
    q,a = level1qs(0,10)
    quesmain.set(q)
    setans(a)
    textabovebox.set("Enter Answer Below")
    score.set(0)
    level.set(1)
    endgamelabel.pack_forget()
    restartbutton.pack_forget()
    endgamebutton.pack_forget()

    displaylevel()

#end game and quit window
def quitgame():
    root.quit()

#give hint
def hint():
    textabovebox.set("The answer starts with '{}'".format(ans.get()[0]))

#set score to max for testing
def cheat():
    score.set(10000)

#GAME Code

#intro items
startbutton = Button(root, text= "Lets Play!" , padx = 30, pady = 30,command=instructions,fg="red",bg="black")
startlabel = Label(root, text="Welcome to Mathematics 101! Here you will practice solving different mathematic equations and improve your calculation skills!")


#instruction items
Text = Label(root,text="Solve each question and input your answers rounded off to 2 decimal places(ONLY INPUT NUMBERS). \neg. 3.15923 should be entered as 3.16")
Begin = Button(root,text = "Click to begin!",command = displaylevel)

#question items
quesmain = StringVar()
ans = StringVar()
textabovebox = StringVar()

#scoring items
score = IntVar()
level = IntVar()
q = ""
q,a = level1qs(0,10)
quesmain.set(q)
setans(a)
textabovebox.set("Enter Answer Below")
score.set(0)
level.set(1)

#Define each widget 
qsword = Label(root,text="Question:")
qslabel = Label(root,textvariable = quesmain)
scorelabel = Label(root, textvariable=  score)
levellabel = Label(root, textvariable= level)
answer = Entry(root, width= 30,borderwidth=1)
wordans = Label(root,textvariable=textabovebox)
submit = Button(root, text = "Submit" ,padx = 30, pady = 30,command=checkans)
scorewordlabel = Label(root,text="Score:")
levelwordlabel = Label(root,text="Level:")
endgamelabel = Label(root,text = "Congratulations! You have finished all the levels of the game! \n What would you like to do next?")
restartbutton = Button(root,text="Play Again!",command = playagain)
endgamebutton = Button(root,text="Quit Game",command=quitgame)
hintbutton = Button(root,text="Hint?",command=hint)
cheatbutton = Button(root, text="Cheat", command=cheat)

#Game Start
intro()
    
root.title("Simple Math Game")

root.geometry("1000x500+200+130")

root.mainloop()